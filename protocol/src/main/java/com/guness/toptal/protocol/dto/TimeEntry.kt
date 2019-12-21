package com.guness.toptal.protocol.dto

import androidx.room.PrimaryKey
import com.guness.toptal.protocol.aliases.R_Entity
import com.guness.toptal.protocol.aliases.S_Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@S_Entity
@R_Entity
data class TimeEntry(
    @Id
    @PrimaryKey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val userUid: String,
    val name: String,
    val timeZone: String //DateTimeZone
)
