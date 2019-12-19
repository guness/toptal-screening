package com.guness.toptal.client.utils.gson

import com.guness.toptal.client.utils.extensions.DATETIME_YEAR_MONTH_FORMAT

class YearMonthSerializer : BaseDateTimeSerializer() {
    override val formatter by lazy { DATETIME_YEAR_MONTH_FORMAT }
}