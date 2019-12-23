package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.joda.time.DateTimeUtils
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
) {
    
    @Ignore
    val diff = DateTimeUtils.currentTimeMillis().let { (DateTimeZone.getDefault().getOffset(it) - timeZone.getOffset(it)) / (1000 * 60) }

    @Ignore
    val name: String = timeZone.getName(DateTimeUtils.currentTimeMillis())

    @Ignore
    val city: String = timeZone.id.split("/").getOrNull(1)?.replace("_", " ") ?: "-"
}

val validTimeZoneIDs by lazy {
    DateTimeZone.getAvailableIDs()
        .filter { it.contains("/") && !it.startsWith("Etc/") }
}