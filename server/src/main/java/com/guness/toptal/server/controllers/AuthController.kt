package com.guness.toptal.server.controllers

import com.guness.toptal.protocol.request.LoginRequest
import com.guness.toptal.protocol.response.LoginResponse
import com.guness.toptal.server.model.Auth
import com.guness.toptal.server.repositories.AuthRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController("/auth")
class AuthController(val repository: AuthRepository) {

    @GetMapping("/test")
    fun list() = repository.findAll()

    @PostMapping("/test")
    fun testCreate() {
        repository.save(Auth(userId = UUID.randomUUID().toString(), token = "random token"))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse = TODO()

    @PostMapping("/logout")
    fun logout() = Unit

}