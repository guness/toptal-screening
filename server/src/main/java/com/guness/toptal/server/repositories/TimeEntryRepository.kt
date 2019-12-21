package com.guness.toptal.server.repositories

import com.guness.toptal.server.model.StoredTimeEntry
import com.guness.toptal.server.model.StoredUser
import org.springframework.data.repository.CrudRepository

interface TimeEntryRepository : CrudRepository<StoredTimeEntry, Long> {
    fun findByUser(user: StoredUser): List<StoredTimeEntry>
}