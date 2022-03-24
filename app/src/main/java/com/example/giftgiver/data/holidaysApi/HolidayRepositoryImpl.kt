package com.example.giftgiver.data.holidaysApi

import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.domain.repositories.HolidayRepository

class HolidayRepositoryImpl(private val api: HolidayApi, private val holidayMapper: HolidayMapper) :
    HolidayRepository {
    override suspend fun getHolidays(year: String) =  holidayMapper.map(api.getHolidays(year))
}
