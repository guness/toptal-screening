package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonIgnore
import com.guness.toptal.protocol.util.Exclude
import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import java.io.Serializable
import kotlin.math.absoluteValue

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onUpdate = ForeignKey.SET_NULL,
        onDelete = ForeignKey.CASCADE
    )]
)
data class TimeEntry(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long? = null,
    val timeZone: DateTimeZone
) : Serializable {

    @Ignore
    @Exclude
    @JsonIgnore
    val diff = timeZone.diff.run { "${sign(this)}${formatTime(absoluteValue)}" }

    @Ignore
    @Exclude
    @JsonIgnore
    val name: String = timeZone.name

    @Ignore
    @Exclude
    @JsonIgnore
    val city: String = timeZone.id.timeZoneCity()
}

fun String.timeZoneCity() = split("/").last().replace("_", " ")

val DateTimeZone.name: String
    get() = getName(DateTimeUtils.currentTimeMillis())

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
        .sortedBy { it.timeZoneCity() }
}