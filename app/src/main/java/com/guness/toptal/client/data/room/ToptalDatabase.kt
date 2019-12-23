package com.guness.toptal.client.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guness.toptal.client.di.RoomModule
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User

@Database(
    entities = [
        User::class,
        TimeEntry::class
    ], version = RoomModule.version
)
@TypeConverters(DateConverter::class, EnumsConverter::class)
abstract class ToptalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun entryDao(): EntryDao

    companion object {
        const val TABLE_USER = "User"
        const val TABLE_ENTRY = "TimeEntry"

    }
}
