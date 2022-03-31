package com.example.giftgiver.features.calendar.domain.useCases

import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCalendarUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, events: List<Event>) =
        withContext(dispatcher) {
            clientsRepository.updateCalendar(vkId, events)
        }
}
