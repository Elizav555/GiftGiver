package com.example.giftgiver.features.gift.domain.useCases

import android.util.Log
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.repositories.GiftsRepOffline
import com.example.giftgiver.features.gift.domain.repositories.GiftsRepository
import com.example.giftgiver.features.images.domain.DeleteImageUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.utils.AppDispatchers
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class GiftUseCase @Inject constructor(
    private val giftsRepository: GiftsRepository,
    private val giftsRepOffline: GiftsRepOffline,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val dispatcher: AppDispatchers,
) {
    suspend fun addGift(vkId: Long, gift: Gift, wishlists: List<Wishlist>) =
        withContext(dispatcher.io) {
            giftsRepository.addGift(vkId, gift, wishlists)
            giftsRepOffline.addGift(gift)
        }

    suspend fun deleteGift(vkId: Long, gift: Gift, wishlists: List<Wishlist>) =
        withContext(dispatcher.io) {
            giftsRepository.deleteGift(vkId, gift, wishlists)
            giftsRepOffline.deleteGift(gift)
            gift.imageUrl?.let { deleteImageUseCase(it) }
        }

    suspend fun updateGift(vkId: Long, gift: Gift) =
        withContext(dispatcher.io) {
            giftsRepository.updateGift(vkId, gift)
            giftsRepOffline.addGift(gift)
        }

    suspend fun getGift(vkId: Long, giftId: String) =
        withContext(dispatcher.io)
        {
            try {
                giftsRepository.getGift(vkId, giftId)
            } catch (ex: Exception) {
                if (ex is UnknownHostException || (ex is FirebaseFirestoreException && ex.message?.contains(
                        "offline"
                    ) == true)
                ) {
                    Log.e("Internet", ex.toString())
                    giftsRepOffline.getGift(giftId)
                } else {
                    null
                }
            }
        }
}
