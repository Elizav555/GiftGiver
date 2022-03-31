package com.example.giftgiver.features.calendar.domain

import com.example.giftgiver.features.event.domain.Event

interface HolidayRepository {
    suspend fun getHolidays(year: String): List<Event>
}
