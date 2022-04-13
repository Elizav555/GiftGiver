package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddClientUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsOffline: ClientsRepOffline,
    private val clientStateUseCase: GetClientStateUseCase,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(client: Client) =
        withContext(dispatcher) {
            clientsRepository.addClient(client)
            clientsOffline.addClient(client)
            clientStateUseCase.addClient(client)
        }
}
