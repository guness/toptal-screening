package com.guness.toptal.client.utils.extensions

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


val DATE_DASH_DATE_FORMAT: DateFormat by lazy { "yyyy-MM-dd".dateFormat() }

val DATE_SLASH_DATE_FORMAT: DateFormat by lazy { "MM/dd/yyyy".dateFormat() }

val DATE_SLASH_MONTH_YEAR_FORMAT: DateFormat by lazy { "MM/yyyy".dateFormat() }

val DATE_PRETTY_DATE_FORMAT: DateFormat by lazy { "MMM dd, yyyy".dateFormat() }

val DATE_PRETTY_MONTH_YEAR_FORMAT: DateFormat by lazy { "MMM yyyy".dateFormat() }

val DATE_PRETTY_DATE_TIME_FORMAT: DateFormat by lazy { "MMM dd, yyyy, h:mm a".dateFormat() }

val DATE_ISO_FORMAT: DateFormat by lazy { "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".dateFormat() }

val DATE_ISO8601_FORMAT: DateFormat by lazy { "yyyy-MM-dd'T'HH:mm:ss'Z'".dateFormat() }

val DATE_TIMEZONE_FORMAT: DateFormat by lazy { "ZZZZZ".dateFormat() }

val DATE_IMG_FORMAT: DateFormat by lazy { "yyyyMMdd_HHmmss".dateFormat() }

fun Date.dashDateString() = DATE_DASH_DATE_FORMAT.format(this)

fun Date.slashDateString() = DATE_SLASH_DATE_FORMAT.format(this)

fun Date.slashMonthYearString() = DATE_SLASH_MONTH_YEAR_FORMAT.format(this)

fun Date.prettyDateString() = DATE_PRETTY_DATE_FORMAT.format(this)

fun Date.prettyMonthYearString() = DATE_PRETTY_MONTH_YEAR_FORMAT.format(this)

fun Date.prettyDateTimeString() = DATE_PRETTY_DATE_TIME_FORMAT.format(this)

fun Date.isoString() = DATE_ISO_FORMAT.format(this)

fun Date.timeZoneDesignator() = DATE_TIMEZONE_FORMAT.format(this)

fun Date.imgString() = "IMG_${DATE_IMG_FORMAT.format(this)}"

fun String.dateFormat(timeZone: TimeZone = TimeZone.getDefault()) =
    SimpleDateFormat(this, Locale.US).apply { this.timeZone = timeZone }
