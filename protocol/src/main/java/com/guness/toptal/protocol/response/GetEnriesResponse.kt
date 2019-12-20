package com.guness.toptal.protocol.response

import com.guness.toptal.protocol.dto.TimeEntry

data class GetEnriesResponse(val entries: List<TimeEntry>)