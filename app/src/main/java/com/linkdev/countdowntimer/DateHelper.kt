package com.linkdev.countdowntimer

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val TINE_FORMAT = "mm:ss"
    fun formatCalendarToString(
        calendar: Calendar,
        format: String
    ): String {
        val mFormatter = SimpleDateFormat(format, Locale.getDefault())
        var text: String = calendar.time.toString()
        try {
            text = (mFormatter.format(calendar.time)) ?: ""
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
        return text
    }
}