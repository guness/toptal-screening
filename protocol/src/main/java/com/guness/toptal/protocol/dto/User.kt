package com.guness.toptal.protocol.dto

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
    @Id
    val uid: String,
    val name: String,
    val type: UserType
)
