package com.guness.toptal.server.service

import com.guness.toptal.server.model.DetailedUser
import com.guness.toptal.server.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository?.findByUsername(username)?.let(::DetailedUser)
            ?: throw UsernameNotFoundException(String.format("No user found with username '%s'.", username))
    }
}