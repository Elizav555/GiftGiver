package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.repositories.ClientsRepository
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
