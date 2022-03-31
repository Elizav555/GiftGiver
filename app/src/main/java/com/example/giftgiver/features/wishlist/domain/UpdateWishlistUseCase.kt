package com.example.giftgiver.features.wishlist.domain

import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateWishlistUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, wishlists: List<Wishlist>) =
        withContext(dispatcher) {
            clientsRepository.updateWishlists(vkId, wishlists)
        }
}
