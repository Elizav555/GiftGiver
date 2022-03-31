package com.example.giftgiver.features.calendar.data.holidaysApi

import com.example.giftgiver.features.calendar.data.mappers.HolidayMapper
import com.example.giftgiver.features.calendar.domain.HolidayRepository

class HolidayRepositoryImpl(private val api: HolidayApi, private val holidayMapper: HolidayMapper) :
    HolidayRepository {
    override suspend fun getHolidays(year: String) =  holidayMapper.map(api.getHolidays(year))
}
