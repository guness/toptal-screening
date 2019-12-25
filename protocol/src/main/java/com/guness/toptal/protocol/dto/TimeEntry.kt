package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import kotlin.math.absoluteValue

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
    val diff = timeZone.diff.run { "${sign(this)}${formatTime(absoluteValue)}" }

    @Ignore
    val name: String = timeZone.getName(DateTimeUtils.currentTimeMillis())

    @Ignore
    val city: String = timeZone.id.split("/").last().replace("_", " ")
}

private val DateTimeZone.diff
    get() = DateTimeUtils.currentTimeMillis().let { getOffset(it) - (DateTimeZone.getDefault().getOffset(it)) }

private fun sign(time: Int): String {
    return when (time) {
        0 -> ""
        in 1..Int.MAX_VALUE -> "+"
        else -> "-"
    }
}

private fun formatTime(time: Int): String {
    val totalSeconds = time / 1000
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600

    return when {
        hours > 0 && minutes == 0 -> "${hours}h"
        hours == 0 && minutes > 0 -> "${minutes}m"
        hours > 0 && minutes > 0 -> String.format("%d:%02d", hours, minutes)
        else -> "="
    }
}

val validTimeZoneIDs by lazy {
    DateTimeZone.getAvailableIDs()
        .filter { it.contains("/") && !it.startsWith("Etc/") }
}