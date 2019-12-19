package com.guness.toptal.client.utils.gson

import com.guness.toptal.client.utils.extensions.DATE_DASH_DATE_FORMAT

class DateSerializer : BaseDateSerializer() {
    override val formatter get() = DATE_DASH_DATE_FORMAT
}