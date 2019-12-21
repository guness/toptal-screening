package com.guness.toptal.protocol.request

import com.guness.toptal.protocol.dto.UserRole

data class UpdateUserRequest(
    val password: String? = null,
    val role: UserRole? = null
)