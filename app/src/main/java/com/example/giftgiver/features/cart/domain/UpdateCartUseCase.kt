package com.example.giftgiver.features.cart.domain

import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, gifts: List<Gift>) =
        withContext(dispatcher) {
            clientsRepository.updateCart(vkId, gifts)
        }
}
