package com.guness.toptal.client.di

import androidx.room.Room
import androidx.room.migration.Migration
import com.guness.toptal.client.data.room.ToptalDatabase
import com.guness.toptal.client.utils.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun providesToptalDatabase(context: AppContext): ToptalDatabase = Room.databaseBuilder(context, ToptalDatabase::class.java, dbName)
        .addMigrations(*migrations)
        .build()

    @Provides
    fun providesUserDao(database: ToptalDatabase) = database.userDao()

    @Provides
    fun providesEntryDao(database: ToptalDatabase) = database.entryDao()

    companion object {

        const val dbName = "toptal-db"

        const val version = 1

        val migrations = arrayOf<Migration>()
    }
}