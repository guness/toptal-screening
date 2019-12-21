package com.guness.toptal.server.model

import com.guness.toptal.protocol.dto.TimeEntry
import javax.persistence.*

@Entity
@Table(name = "TimeEntry")
data class StoredTimeEntry(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn
    val user: StoredUser,
    val name: String,
    val timeZone: String //DateTimeZone
)

fun StoredTimeEntry.toDto() = TimeEntry(id, user.id, name, timeZone)