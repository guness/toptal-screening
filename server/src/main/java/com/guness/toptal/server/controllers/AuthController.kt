package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.LoginRequest
import com.guness.toptal.protocol.response.LoginResponse
import com.guness.toptal.server.model.DetailedUser
import com.guness.toptal.server.model.StoredUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.repositories.RoleRepository
import com.guness.toptal.server.repositories.UserRepository
import com.guness.toptal.server.security.TokenHelper
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(
    val tokenHelper: TokenHelper,
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        // Inject into security context
        SecurityContextHolder.getContext().authentication = authentication
        // token creation
        val user = authentication.principal as DetailedUser
        val jws = tokenHelper.generateToken(user.username)
        // Return the token
        return LoginResponse(user.toDto(), jws)
    }

    @PostMapping("/auth/logout")
    fun logout() = Unit

    @PostMapping("/auth/register")
    fun register(@RequestBody request: CreateUserRequest): ResponseEntity<*> {
        val user = StoredUser(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            authorities = listOfNotNull(roleRepository.findByRole())
        )
        try {
            userRepository.save(user)
        } catch (e: DataIntegrityViolationException) {
            return ResponseEntity.unprocessableEntity().build<Any>()
        }
        return ResponseEntity.ok(login(LoginRequest(request.username, request.password)))
    }
}