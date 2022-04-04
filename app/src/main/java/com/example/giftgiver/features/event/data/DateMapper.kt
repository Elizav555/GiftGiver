package com.example.giftgiver.features.event.data

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateMapper {
    private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormatShort: DateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    fun parseStringToCalendar(date: String): Calendar {
        val time = try {
            dateFormat.parse(date)?.time
        } catch (ex: ParseException) {
            dateFormatShort.parse(date)?.time
        }
        time?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return calendar
        }
        return Calendar.getInstance()
    }

    fun parseCalendarToString(date: Calendar) = dateFormat.format(date.time)
}
