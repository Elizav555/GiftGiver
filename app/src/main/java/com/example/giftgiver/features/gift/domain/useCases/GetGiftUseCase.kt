package com.example.giftgiver.features.gift.domain.useCases

import com.example.giftgiver.features.gift.domain.GiftsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGiftUseCase @Inject constructor(
    private val giftsRepository: GiftsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, giftId: String) =
        withContext(dispatcher) {
            giftsRepository.getGift(vkId, giftId)
        }
}
