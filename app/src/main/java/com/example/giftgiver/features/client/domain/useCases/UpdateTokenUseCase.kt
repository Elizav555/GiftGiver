package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import javax.inject.Inject

class UpdateTokenUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository
) {
    suspend operator fun invoke(vkId: Long, token: String) {
        clientsRepository.updateClient(vkId, mapOf("pushToken" to token))
    }
}