package com.vanpra.composematerialdialogs.datetime.util

import androidx.compose.ui.text.intl.Locale
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSDateComponents
import platform.Foundation.calendarIdentifier
import platform.Foundation.NSLocale as PlatformLocale

fun LocalTime.toNSDateComponents(): NSDateComponents {
    val components = NSDateComponents()
    components.hour = hour.convert()
    components.minute = minute.convert()
    components.second = second.convert()
    components.nanosecond = nanosecond.convert()
    return components
}

internal fun getCalendar(locale: Locale): NSCalendar {
    val platformLocale = locale.toPlatform()
    return NSCalendar(platformLocale.calendarIdentifier).apply {
        this.locale = platformLocale
    }
}

internal actual val LocalDate.isLeapYear: Boolean
    get() =  if (year % 400 == 0) {
        true
    } else if (year % 100 == 0) {
        false
    } else {
        year % 4 == 0
    }

private fun NSDateComponents.toKotlinInstant() = NSCalendar.currentCalendar.dateFromComponents(this)!!.toKotlinInstant()

internal actual fun LocalTime.minusHours(hoursToSubtract: Long): LocalTime = toNSDateComponents().apply {
    hour -= hoursToSubtract
}.toKotlinInstant().toLocalDateTime(TimeZone.UTC).time
internal actual fun LocalTime.plusHours(hoursToAdd: Long): LocalTime = toNSDateComponents().apply {
    hour += hoursToAdd
}.toKotlinInstant().toLocalDateTime(TimeZone.UTC).time

internal fun Locale.toPlatform() = PlatformLocale(toLanguageTag())

internal actual fun Month.getShortLocalName(locale: Locale): String = getCalendar(locale).shortMonthSymbols()
    .getOrNull(this.ordinal)
    .toString()

internal actual fun Month.getFullLocalName(locale: Locale) =
    getCalendar(locale).monthSymbols()
        .getOrNull(this.ordinal)
        .toString()

fun DayOfWeek.toIosWeekDayInt() = if (this == DayOfWeek.SUNDAY) 0 else this.ordinal + 1

internal actual fun DayOfWeek.getShortLocalName(locale: Locale) = getCalendar(locale).shortWeekdaySymbols()
    .getOrNull(toIosWeekDayInt())
    .toString()

internal actual fun Month.testLength(year: Int, isLeapYear: Boolean): Int {
    val cal = NSCalendar.currentCalendar()
    val dateComponents = NSDateComponents().apply {
        this.year = year.convert()
        month = ordinal.convert()
    }
    val date = cal.dateFromComponents(dateComponents)!!
    return memScoped {
        val range = cal.rangeOfUnit(NSCalendarUnitDay, NSCalendarUnitMonth, date)
        range.size
    }
}

internal actual operator fun DayOfWeek.plus(days: Long): DayOfWeek = NSDateComponents().apply {
    weekday = toIosWeekDayInt().convert()
}.toKotlinInstant().toLocalDateTime(TimeZone.UTC).dayOfWeek


internal actual fun DayOfWeek.getNarrowDisplayName(locale: Locale): String = getCalendar(locale).veryShortWeekdaySymbols()
    .getOrNull(toIosWeekDayInt())
    .toString()