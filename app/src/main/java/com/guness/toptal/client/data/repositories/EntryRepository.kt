package com.guness.toptal.client.data.repositories

import com.guness.toptal.client.data.WebService
import com.guness.toptal.client.data.room.EntryDao
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.request.UpdateEntryRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntryRepository @Inject constructor(
    private val webService: WebService,
    private val entryDao: EntryDao
) {
    fun entries() = entryDao.entries()

    fun deleteEntry(entry: TimeEntry) = webService.deleteEntry(entry.id)
        .doOnComplete { entryDao.deleteEntry(entry) }

    fun createEntry(entry: TimeEntry, userId: Long? = null) = webService.createEntry(entry, userId)
        .doOnSuccess(entryDao::addEntry)

    fun fetchEntries() = webService.getEntries().map { it.entries }
        .doOnSuccess(entryDao::addEntries)

    fun renameEntry(id: Long, name: String) = webService.updateEntry(id, UpdateEntryRequest(name = name))
        .doOnSuccess(entryDao::addEntry)

    fun updateTime(id: Long, timeZone: String) = webService.updateEntry(id, UpdateEntryRequest(timeZone = timeZone))
        .doOnSuccess(entryDao::addEntry)
}