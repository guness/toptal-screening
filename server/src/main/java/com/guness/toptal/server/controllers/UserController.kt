package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.UpdateUserRequest
import com.guness.toptal.protocol.response.GetUsersResponse
import com.guness.toptal.server.model.StoredUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.repositories.RoleRepository
import com.guness.toptal.server.repositories.UserRepository
import com.guness.toptal.server.utils.hasAny
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*

@RestController("/user")
class UserController(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/user")
    fun user(principal: Principal): User? {
        return userRepository.findByUsername(principal.name)?.toDto()
    }

    @GetMapping("/user", params = ["id"])
    @PreAuthorize("hasRole('ADMIN')")
    fun loadById(@RequestParam id: Long): User {
        return userRepository.findById(id).get().toDto()
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasRole('ADMIN') || hasRole('MANAGER')")
    fun loadAll(): GetUsersResponse {
        return GetUsersResponse(when {
            arrayOf(UserRole.ROLE_ADMIN).hasAny() -> {
                userRepository.findAll().map { it.toDto() }
            }
            arrayOf(UserRole.ROLE_MANAGER).hasAny() -> {
                userRepository.findAll().filter { it.authorities.any { it.role == UserRole.ROLE_USER } }.map { it.toDto() }
            }
            else -> {
                emptyList()
            }
        })
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<User> {
        val user = StoredUser(
            name = request.name,
            username = request.username,
            password = passwordEncoder.encode(request.password),
            authorities = listOfNotNull(roleRepository.findByRole())
        )
        return try {
            ResponseEntity.ok(userRepository.save(user).toDto())
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUser(@PathVariable id: Long, @RequestBody request: UpdateUserRequest): ResponseEntity<User> {
        var user = userRepository.findById(id).get()
        request.password?.let {
            user = user.copy(password = passwordEncoder.encode(it))
        }
        request.name?.let {
            user = user.copy(name = it)
        }
        request.role?.let {
            val role = roleRepository.findByRole(it) ?: throw NoSuchElementException("No value present")
            user = user.copy(authorities = listOf(role))
        }
        return try {
            ResponseEntity.ok(userRepository.save(user).toDto())
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity.unprocessableEntity().build()
        }
    }

    @PutMapping("/user")
    fun updateSelf(@RequestBody request: UpdateUserRequest, principal: Principal): ResponseEntity<User> {
        var user = userRepository.findByUsername(principal.name) ?: throw NoSuchElementException("No value present")
        request.password?.let {
            user = user.copy(password = passwordEncoder.encode(request.password))
        }
        request.name?.let {
            user = user.copy(name = it)
        }
        return try {
            ResponseEntity.ok(userRepository.save(user).toDto())
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity.unprocessableEntity().build()
        }
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        return try {
            userRepository.deleteById(id)
            ResponseEntity.ok().build()
        } catch (e: EmptyResultDataAccessException) {
            ResponseEntity.badRequest().build()
        }
    }
}