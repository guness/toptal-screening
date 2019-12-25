package com.guness.toptal.client.data.repositories

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.guness.toptal.client.data.WebService
import com.guness.toptal.client.data.room.EntryDao
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.request.UpdateEntryRequest
import org.joda.time.DateTimeZone
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

    @AnyThread
    fun createEntry(entry: TimeEntry, userId: Long? = null) = webService.createEntry(entry, userId)
        .doOnSuccess(entryDao::addEntry)

    @WorkerThread
    fun addEntry(entry: TimeEntry) = entryDao.addEntry(entry)

    fun fetchEntries() = webService.getEntries().map { it.entries }
        .doOnSuccess(entryDao::setEntries)

    fun updateTime(id: Long, timeZone: DateTimeZone) = webService.updateEntry(id, UpdateEntryRequest(timeZone = timeZone))
        .doOnSuccess(entryDao::addEntry)
}