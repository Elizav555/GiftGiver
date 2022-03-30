package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.domain.repositories.ClientsRepository
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
