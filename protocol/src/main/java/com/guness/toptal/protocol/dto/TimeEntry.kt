package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.DateTimeZone

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )]
)
data class TimeEntry(

    @PrimaryKey
    val id: Long = 0,
    val userId: Long = 0,
    val timeZone: DateTimeZone
)

val validTimeZoneIDs by lazy {
    DateTimeZone.getAvailableIDs()
        .filter { it.contains("/") && !it.startsWith("Etc/") }
}