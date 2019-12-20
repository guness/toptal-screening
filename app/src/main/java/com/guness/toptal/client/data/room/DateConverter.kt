package com.guness.toptal.client.data.room

import androidx.room.TypeConverter
import org.joda.time.DateTime
import java.util.*

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun toDateTime(value: Long?): DateTime? {
        return value?.let { DateTime(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(value: DateTime?): Long? {
        return value?.millis
    }

    @TypeConverter
    @JvmStatic
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDate(value: Date?): Long? {
        return value?.time
    }
}