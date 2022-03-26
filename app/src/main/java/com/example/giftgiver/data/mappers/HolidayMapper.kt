package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.holidaysApi.response.Holiday
import com.example.giftgiver.domain.entities.Event

class HolidayMapper {
    private val dateMapper = DateMapper()
    fun map(holidays: List<Holiday>): List<Event> {
        return holidays.map { holiday ->
            Event(
                date = dateMapper.parseStringToCalendar(holiday.date),
                desc = holiday.localName
            )
        }
    }
}
