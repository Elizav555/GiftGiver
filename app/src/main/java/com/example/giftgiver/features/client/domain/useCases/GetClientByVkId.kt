package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetClientByVkId @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsOffline: ClientRepOffline,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long) =
        withContext(dispatcher) {
            try {
                clientsRepository.getClientByVkId(vkId)
            } catch (ex: Exception) {
                clientsOffline.getClientByVkId(vkId)
            }
        }
}
