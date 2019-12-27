package com.guness.toptal.protocol.request

import org.joda.time.DateTimeZone

data class UpdateEntryRequest(val timeZone: DateTimeZone? = null, val userId: Long? = null)