package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.protocol.request.UpdateEntryRequest
import com.guness.toptal.protocol.response.GetEntriesResponse
import com.guness.toptal.server.model.StoredTimeEntry
import com.guness.toptal.server.model.StoredUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.repositories.TimeEntryRepository
import com.guness.toptal.server.repositories.UserRepository
import com.guness.toptal.server.utils.hasAny
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.transaction.Transactional

@RestController("/zone")
class TimeEntryController(
    val userRepository: UserRepository,
    val timeEntryRepository: TimeEntryRepository
) {

    @GetMapping("/zone")
    fun entries(user: Principal): GetEntriesResponse {
        return GetEntriesResponse(if (arrayOf(UserRole.ROLE_MANAGER, UserRole.ROLE_ADMIN).hasAny()) {
            timeEntryRepository.findAll()
        } else {
            userRepository.findByUsername(user.name)?.timeEntries
        }?.map { it.toDto() } ?: emptyList())
    }

    @GetMapping("/zone", params = ["userId"])
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
    fun entries(@RequestParam userId: Long): GetEntriesResponse {
        return GetEntriesResponse(userRepository.findById(userId).get().timeEntries.map { it.toDto() })
    }

    @PostMapping("/zone", params = ["userId"])
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
    fun createEntry(@RequestParam userId: Long, @RequestBody request: TimeEntry): TimeEntry {
        val storedUser = userRepository.findById(userId).get()
        return saveTimeEntry(storedUser, request.name, request.timeZone)
    }

    @PostMapping("/zone")
    fun createEntry(@RequestBody request: TimeEntry, principal: Principal): TimeEntry {
        val storedUser = userRepository.findByUsername(principal.name) ?: throw NoSuchElementException("No value present")
        return saveTimeEntry(storedUser, request.name, request.timeZone)
    }

    @PutMapping("/zone", params = ["id"])
    fun updateEntry(@RequestParam id: Long, @RequestBody request: UpdateEntryRequest, principal: Principal): ResponseEntity<TimeEntry> {
        var entry = timeEntryRepository.findById(id).get()
        if (!arrayOf(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER).hasAny()) {
            if (principal.name != entry.user.username) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        }

        request.name?.let {
            entry = entry.copy(name = it)
        }
        request.timeZone?.let {
            entry = entry.copy(timeZone = it)
        }
        return ResponseEntity.ok(saveTimeEntry(entry))
    }

    @Transactional
    @DeleteMapping("/zone/{id}")
    fun deleteEntry(@PathVariable id: Long, principal: Principal): ResponseEntity<Unit> {
        return try {
            if (arrayOf(UserRole.ROLE_ADMIN, UserRole.ROLE_MANAGER).hasAny()) {
                timeEntryRepository.deleteById(id)
            } else {
                val storedUser = userRepository.findByUsername(principal.name) ?: throw NoSuchElementException("No value present")
                timeEntryRepository.deleteByIdAndUserId(id, storedUser.id)
            }
            ResponseEntity.ok().build()
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.badRequest().build()
        }
    }

    private fun saveTimeEntry(user: StoredUser, name: String, timeZone: String) =
        saveTimeEntry(StoredTimeEntry(user = user, name = name, timeZone = timeZone))

    private fun saveTimeEntry(entry: StoredTimeEntry) = timeEntryRepository.save(entry).toDto()
}