package com.example.giftgiver.data.mappers

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateMapper {
    private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun parseStringToCalendar(date: String): Calendar {
        val time = dateFormat.parse(date)?.time
        time?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return calendar
        }
        return Calendar.getInstance()
    }
}
