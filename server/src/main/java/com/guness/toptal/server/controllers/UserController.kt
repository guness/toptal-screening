package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.server.model.StoredUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.repositories.RoleRepository
import com.guness.toptal.server.repositories.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController("/user")
class UserController(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/user/current")
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN') || hasRole('MANAGER')")
    fun user(user: Principal): User? {
        return userRepository.findByUsername(user.name)?.toDto()
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun loadById(@PathVariable userId: Long): User? {
        return userRepository.findById(userId).get().toDto()
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun loadAll(): List<User?> {
        return userRepository.findAll().map { it.toDto() }
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(request: CreateUserRequest): ResponseEntity<User> {
        val user = StoredUser(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            authorities = listOfNotNull(roleRepository.findByRole())
        )
        return try {
            ResponseEntity.ok(userRepository.save(user).toDto())
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity.unprocessableEntity().build()
        }
    }
}