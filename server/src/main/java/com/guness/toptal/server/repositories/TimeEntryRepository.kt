package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.StoredTimeEntry
import com.guness.toptal.server.model.StoredUser
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository

interface TimeEntryRepository : CrudRepository<StoredTimeEntry, Long>, Repository<StoredTimeEntry, Long> {
    fun findByUser(user: StoredUser): List<StoredTimeEntry>
    fun deleteByIdAndUserId(id: Long, userId: Long)
}