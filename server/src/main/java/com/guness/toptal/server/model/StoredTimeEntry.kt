package com.guness.toptal.server.model

import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.validTimeZoneIDs
import org.joda.time.DateTimeZone
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
    val timeZone: DateTimeZone
)

fun StoredTimeEntry.toDto() = TimeEntry(id, user.id, timeZone)

val DateTimeZone.isValid: Boolean
    get() = validTimeZoneIDs.contains(this.id)