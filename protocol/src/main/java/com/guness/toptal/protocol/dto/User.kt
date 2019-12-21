package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Long? = null,
    val username: String,
    val role: UserRole
)