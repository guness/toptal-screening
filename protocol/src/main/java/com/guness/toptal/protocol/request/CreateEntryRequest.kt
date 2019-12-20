package com.guness.toptal.protocol.request

import org.joda.time.DateTimeZone

data class CreateEntryRequest(val userUid: String? = null, val timeZone: DateTimeZone)