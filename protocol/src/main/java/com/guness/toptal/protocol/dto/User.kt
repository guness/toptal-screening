package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Long = 0,
    val name: String,
    val username: String,
    val role: UserRole
)