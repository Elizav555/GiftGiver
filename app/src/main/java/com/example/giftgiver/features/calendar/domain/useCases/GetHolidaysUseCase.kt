package com.example.giftgiver.features.calendar.domain.useCases

import com.example.giftgiver.features.calendar.domain.HolidayRepository
import com.example.giftgiver.features.event.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHolidaysUseCase @Inject constructor(
    private val holidayRepository: HolidayRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(year: String?): List<Event> {
        val curYear =
            year ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()
        return withContext(dispatcher) {
            holidayRepository.getHolidays(curYear)
        }
    }
}
