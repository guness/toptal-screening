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

    @WorkerThread
    fun clear() = entryDao.clear()

    @AnyThread
    fun createEntry(entry: TimeEntry, userId: Long? = null) = webService.createEntry(entry, userId)
        .doOnSuccess(entryDao::addEntry)

    @AnyThread
    fun deleteEntry(entry: TimeEntry) = webService.deleteEntry(entry.id)
        .doOnComplete { removeEntry(entry) }

    @WorkerThread
    fun addEntry(entry: TimeEntry) = entryDao.addEntry(entry)

    @WorkerThread
    fun removeEntry(entry: TimeEntry) = entryDao.deleteEntry(entry)

    @AnyThread
    fun fetchEntries() = webService.getEntries().map { it.entries }
        .doOnSuccess(entryDao::setEntries)

    fun updateTime(id: Long, timeZone: DateTimeZone, userId: Long? = null) = webService.updateEntry(id, UpdateEntryRequest(timeZone = timeZone, userId = userId))
        .doOnSuccess(entryDao::addEntry)
}