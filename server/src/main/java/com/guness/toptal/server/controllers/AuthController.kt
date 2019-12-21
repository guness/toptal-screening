package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.request.LoginRequest
import com.guness.toptal.protocol.response.LoginResponse
import com.guness.toptal.server.model.DetailedUser
import com.guness.toptal.server.model.toDto
import com.guness.toptal.server.security.TokenHelper
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/auth")
class AuthController(
    val tokenHelper: TokenHelper,
    val authenticationManager: AuthenticationManager
) {

    @PostMapping("/auth/logout")
    fun logout() = Unit


    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<*>? {
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
        return ResponseEntity.ok(LoginResponse(user.toDto(), jws))
    }

}