package com.guness.toptal.protocol.response

import com.guness.toptal.protocol.dto.User

data class GetUsersResponse(val users: List<User>)