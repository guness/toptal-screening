package com.guness.toptal.client.utils.gson

import com.guness.toptal.client.utils.extensions.DATETIME_YEAR_MONTH_DAY_FORMAT

class YearMonthDaySerializer : BaseDateTimeSerializer() {
    override val formatter by lazy { DATETIME_YEAR_MONTH_DAY_FORMAT }
}