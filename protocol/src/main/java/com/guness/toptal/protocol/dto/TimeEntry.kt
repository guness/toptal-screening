package com.guness.toptal.protocol.dto

import org.joda.time.DateTimeZone

data class TimeEntry(val uid: String, val userUid: String, val name: String, val timeZone: DateTimeZone)
