package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeEntry(

    @PrimaryKey
    val id: Long = 0,
    val userId: Long = 0,
    val name: String,
    val timeZone: String //DateTimeZone
)
