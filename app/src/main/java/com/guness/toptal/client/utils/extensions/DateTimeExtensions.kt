package com.guness.toptal.client.utils.extensions

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

val locale: Locale
    get() = Locale.getDefault()

val DATETIME_TIME_ONLY_FORMAT = DateTimeFormat.forPattern("hh:mm a").withLocale(locale)

val DATETIME_US_PRETTY_DATE_TIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy h:mm a").withLocale(locale)

val DATETIME_ISO_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withLocale(locale)

val DATETIME_PRETTY_WEEK_MONTH_DAY_FORMAT = DateTimeFormat.forPattern("EEE, MMM d").withLocale(locale)

val DATETIME_PRETTY_MONTH_YEAR_FORMAT = DateTimeFormat.forPattern("MMMM yyyy").withLocale(locale)

val DATETIME_PRETTY_MONTH_DAY_SHORT_FORMAT = DateTimeFormat.forPattern("MMM d").withLocale(locale)

val DATETIME_PRETTY_DAY_MONTH_YEAR_FORMAT = DateTimeFormat.forPattern("d MMM yyyy").withLocale(locale)

val DATETIME_YEAR_MONTH_DAY_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(locale)

val DATETIME_YEAR_MONTH_FORMAT = DateTimeFormat.forPattern("yyyy-MM").withLocale(locale)

fun DateTime.timeOnlyString() = DATETIME_TIME_ONLY_FORMAT.print(this)

fun DateTime.usPrettyDateTimeString() = DATETIME_US_PRETTY_DATE_TIME_FORMAT.print(this)

fun DateTime.isoString() = DATETIME_ISO_FORMAT.print(this)

fun DateTime.prettyWeekMonthDayString() = DATETIME_PRETTY_WEEK_MONTH_DAY_FORMAT.print(this)

fun DateTime.prettyMonthYearString() = DATETIME_PRETTY_MONTH_YEAR_FORMAT.print(this)

fun DateTime.prettyMonthDayShortString() = DATETIME_PRETTY_MONTH_DAY_SHORT_FORMAT.print(this)

fun DateTime.prettyDayMonthYearString() = DATETIME_PRETTY_DAY_MONTH_YEAR_FORMAT.print(this)

fun DateTime.yearMonthDayString() = DATETIME_YEAR_MONTH_DAY_FORMAT.print(this)

fun DateTime.yearMonthString() = DATETIME_YEAR_MONTH_FORMAT.print(this)

val DateTime.dayTimestamp get() = withMillisOfDay(0).millis

val DateTime.startOfMonth: DateTime
    get() = withDayOfMonth(1).withTimeAtStartOfDay()

val DateTime.endOfMonth: DateTime
    get() = withDayOfMonth(1).plusMonths(1).minusDays(1).withTimeAtStartOfDay()

fun DateTime.sameMonth(dateTime: DateTime) = this.year == dateTime.year && this.monthOfYear == dateTime.monthOfYear
