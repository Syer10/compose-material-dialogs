package com.vanpra.composematerialdialogs.datetime.util

import androidx.compose.ui.text.intl.Locale
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.datetime.DayOfWeek
import platform.Foundation.NSCalendar

internal actual class WeekFields(private val calendar: NSCalendar) {
    @OptIn(ExperimentalForeignApi::class)
    actual val firstDayOfWeek: DayOfWeek
        get() = DayOfWeek(calendar.firstWeekday.convert<Int>().toIsoWeekday())

    actual companion object {
        actual fun of(locale: Locale): WeekFields {
            return WeekFields(getCalendar(locale))
        }
    }
}

private fun Int.toIsoWeekday() = if (this == 1) {
    7 // Sunday is 1 for NSCalendar
} else {
    this - 1
}