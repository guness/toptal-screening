package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.StoredUser
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository

interface UserRepository : CrudRepository<StoredUser, Long>, Repository<StoredUser, Long> {
    fun findByUsername(username: String): StoredUser?
    fun findByTimeEntriesNotNull(): Set<StoredUser>
}