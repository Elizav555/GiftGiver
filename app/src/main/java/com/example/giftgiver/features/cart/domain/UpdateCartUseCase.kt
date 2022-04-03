package com.example.giftgiver.features.cart.domain

import com.example.giftgiver.features.client.domain.ClientsRepository
import com.example.giftgiver.features.gift.domain.GiftInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, giftsIds: List<GiftInfo>) =
        withContext(dispatcher)
        {
            clientsRepository.updateCart(vkId, giftsIds)
        }
}
