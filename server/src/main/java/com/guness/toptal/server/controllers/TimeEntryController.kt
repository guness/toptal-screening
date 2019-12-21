package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.protocol.response.GetEntriesResponse
import com.guness.toptal.server.model.StoredTimeEntry
import com.guness.toptal.server.model.StoredUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.repositories.TimeEntryRepository
import com.guness.toptal.server.repositories.UserRepository
import com.guness.toptal.server.utils.hasAny
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

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
    fun createEntry(@RequestParam userId: Long, @RequestBody request: TimeEntry): TimeEntry? {
        val storedUser = userRepository.findById(userId).get()
        return saveTimeEntry(storedUser, request.name, request.timeZone)
    }

    @PostMapping("/zone")
    fun createEntry(@RequestBody request: TimeEntry, user: Principal): TimeEntry? {
        val storedUser = userRepository.findByUsername(user.name) ?: throw NoSuchElementException("No value present")
        return saveTimeEntry(storedUser, request.name, request.timeZone)
    }

    private fun saveTimeEntry(user: StoredUser, name: String, timeZone: String) =
        timeEntryRepository.save(StoredTimeEntry(user = user, name = name, timeZone = timeZone)).toDto()
}