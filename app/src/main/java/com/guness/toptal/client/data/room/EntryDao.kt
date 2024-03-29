package com.guness.toptal.client.data.room

import androidx.room.*
import com.guness.toptal.protocol.dto.TimeEntry
import io.reactivex.Flowable

@Dao
interface EntryDao {

    @Query("SELECT * FROM ${ToptalDatabase.TABLE_ENTRY} ORDER BY id DESC")
    fun entries(): Flowable<List<TimeEntry>>

    @Query("SELECT * FROM ${ToptalDatabase.TABLE_ENTRY} WHERE userId=:userId ORDER BY id DESC")
    fun entriesByUser(userId: Long): Flowable<List<TimeEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEntries(list: List<TimeEntry>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addEntry(entry: TimeEntry)

    @Delete
    fun deleteEntry(entry: TimeEntry)

    @Delete
    fun deleteEntries(list: List<TimeEntry>)

    @Query("DELETE FROM ${ToptalDatabase.TABLE_ENTRY}")
    fun clear()

    @Query("DELETE FROM ${ToptalDatabase.TABLE_ENTRY} WHERE userId=:userId")
    fun clearByUser(userId: Long)

    @Transaction
    fun setEntries(list: List<TimeEntry>) {
        clear()
        addEntries(list)
    }
}
