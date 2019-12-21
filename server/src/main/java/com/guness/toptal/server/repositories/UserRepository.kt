package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
    override fun findById(id: Long?): Optional<User>
    override fun findAll(): List<User>
}