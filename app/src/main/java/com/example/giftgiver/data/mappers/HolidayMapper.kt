package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.holidaysApi.response.Holiday
import com.example.giftgiver.domain.entities.Event
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HolidayMapper {
    private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    fun map(holidays: List<Holiday>): List<Event> {
        return holidays.map { holiday ->
            Event(
                name = holiday.localName,
                date = parseDateToCalendar(holiday.date),
                desc = holiday.localName
            )
        }
    }

    private fun parseDateToCalendar(date: String): Calendar {
        val time = dateFormat.parse(date)?.time
        time?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time
            return calendar
        }
        return Calendar.getInstance()
    }
}
