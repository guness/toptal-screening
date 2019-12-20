package com.guness.toptal.server.repositories

import com.guness.toptal.protocol.dto.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, String> {
    fun countByName(name: String): Long
}