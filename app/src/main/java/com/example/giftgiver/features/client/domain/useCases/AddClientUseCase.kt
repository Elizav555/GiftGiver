package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.utils.AppDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddClientUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsOffline: ClientsRepOffline,
    private val dispatcher: AppDispatchers,
) {
    suspend operator fun invoke(client: Client) =
        withContext(dispatcher.io) {
            clientsRepository.addClient(client)
            clientsOffline.addClient(client)
        }
}
