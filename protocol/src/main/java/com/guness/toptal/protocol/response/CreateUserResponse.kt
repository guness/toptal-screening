package com.guness.toptal.protocol.response

import com.guness.toptal.protocol.dto.User

data class CreateUserResponse(val user: User, val token: String)