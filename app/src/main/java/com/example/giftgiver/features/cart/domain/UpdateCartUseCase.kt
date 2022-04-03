package com.example.giftgiver.features.cart.domain

import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, giftsIds: List<Pair<String, Long>>) =
        withContext(dispatcher)
        {
            clientsRepository.updateCart(vkId, giftsIds)
        }
}
