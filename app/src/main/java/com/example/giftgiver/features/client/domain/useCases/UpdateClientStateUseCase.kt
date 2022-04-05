package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateClientStateUseCase @Inject constructor(
    private val clientStateRep: ClientStateRep,
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(client: Client) =
        withContext(dispatcher) {
            clientStateRep.addClient(client)
        }
}
