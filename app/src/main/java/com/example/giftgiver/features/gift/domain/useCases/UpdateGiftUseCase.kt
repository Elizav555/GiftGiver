package com.example.giftgiver.features.gift.domain.useCases

import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateGiftUseCase @Inject constructor(
    private val giftsRepository: GiftsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, gift: Gift) =
        withContext(dispatcher) {
            giftsRepository.updateGift(vkId, gift)
        }
}
