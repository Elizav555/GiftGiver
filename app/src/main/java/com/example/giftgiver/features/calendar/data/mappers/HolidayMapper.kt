package com.example.giftgiver.features.calendar.data.mappers

import com.example.giftgiver.features.calendar.data.holidaysApi.response.Holiday
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.event.domain.Event

class HolidayMapper(private val dateMapper: DateMapper) {
    fun map(holidays: List<Holiday>): List<Event> {
        return holidays.map { holiday ->
            Event(
                date = dateMapper.parseStringToCalendar(holiday.date),
                desc = holiday.name //change to localName if want Russian
            )
        }
    }
}
