package com.example.giftgiver.features.gift.domain.useCases

import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftsRepository
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GiftUseCase @Inject constructor(
    private val giftsRepository: GiftsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun addGift(vkId: Long, gift: Gift, wishlists: List<Wishlist>) =
        withContext(dispatcher) {
            giftsRepository.addGift(vkId, gift, wishlists)
        }

    suspend fun deleteGift(vkId: Long, gift: Gift, wishlists: List<Wishlist>) =
        withContext(dispatcher) {
            giftsRepository.deleteGift(vkId, gift, wishlists)
        }

    suspend fun updateGift(vkId: Long, gift: Gift) =
        withContext(dispatcher) {
            giftsRepository.updateGift(vkId, gift)
        }

    suspend fun getGift(vkId: Long, giftId: String) =
        withContext(dispatcher)
        {
            giftsRepository.getGift(vkId, giftId)
        }
}
