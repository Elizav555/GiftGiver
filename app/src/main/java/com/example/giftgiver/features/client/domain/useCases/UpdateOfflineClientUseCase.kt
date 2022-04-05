package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateOfflineClientUseCase @Inject constructor(
    private val clientsOffline: ClientsRepOffline,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(client: Client) =
        withContext(dispatcher) {
            clientsOffline.updateClient(client)
        }
}

