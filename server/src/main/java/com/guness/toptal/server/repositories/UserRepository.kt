package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.StoredUser
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<StoredUser, Long> {
    fun findByUsername(username: String): StoredUser?
}