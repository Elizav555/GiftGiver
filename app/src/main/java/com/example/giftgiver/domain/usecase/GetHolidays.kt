package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.repositories.HolidayRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class GetHolidays(
    private val holidayRepository: HolidayRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    suspend operator fun invoke(year: String?): List<Event>? {
        val curYear = year ?: Calendar.getInstance().get(Calendar.YEAR).toString()
        return withContext(dispatcher) {
            holidayRepository.getHolidays(curYear)
        }
    }
}
