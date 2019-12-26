package com.guness.toptal.protocol.dto

enum class UserRole {
    ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
}

val UserRole.manager
    get() = when (this) {
        UserRole.ROLE_MANAGER, UserRole.ROLE_ADMIN -> true
        else -> false
    }