package com.guness.toptal.protocol.response

import com.guness.toptal.protocol.dto.TimeEntry

data class GetEntriesResponse(val entries: List<TimeEntry>)