package com.example.giftgiver.features.gift.domain.useCases

import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftsRepository
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddGiftUseCase @Inject constructor(
    private val giftsRepository: GiftsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, gift: Gift, wishlists: List<Wishlist>) =
        withContext(dispatcher) {
            giftsRepository.addGift(vkId, gift, wishlists)
        }
}
