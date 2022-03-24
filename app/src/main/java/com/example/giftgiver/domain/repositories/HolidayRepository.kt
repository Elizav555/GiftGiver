package com.example.giftgiver.domain.repositories

import com.example.giftgiver.domain.entities.Event

interface HolidayRepository {
    suspend fun getHolidays(year: String): List<Event>
}
