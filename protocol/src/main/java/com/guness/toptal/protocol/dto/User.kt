package com.guness.toptal.protocol.dto

import com.guness.toptal.protocol.aliases.R_Entity
import com.guness.toptal.protocol.aliases.R_PrimaryKey
import com.guness.toptal.protocol.aliases.S_Entity
import com.guness.toptal.protocol.aliases.S_Id

@S_Entity
@R_Entity
data class User(
    @S_Id
    @R_PrimaryKey
    val uid: String,
    val name: String,
    val type: UserType
)