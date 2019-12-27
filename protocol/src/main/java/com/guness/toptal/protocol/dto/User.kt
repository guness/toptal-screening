package com.guness.toptal.protocol.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey
    val id: Long = 0,
    val name: String,
    val username: String,
    val role: UserRole
) : Serializable