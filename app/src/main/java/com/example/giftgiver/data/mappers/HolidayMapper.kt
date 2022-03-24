package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.holidaysApi.response.Holiday
import com.example.giftgiver.domain.entities.Event
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HolidayMapper {
    fun map(holidays: List<Holiday>): List<Event> {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return holidays.map { holiday ->
            Event(
                name = holiday.localName,
                date = dateFormat.parse(holiday.date) ?: Calendar.getInstance().time,
                desc = null
            )
        }
    }
}