package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientFBUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun updateCart(vkId: Long, giftsIds: List<GiftInfo>) =
        withContext(dispatcher) {
            clientsRepository.updateCart(vkId, giftsIds)
        }

    suspend fun updateCalendar(vkId: Long, events: List<Event>) =
        withContext(dispatcher) {
            clientsRepository.updateCalendar(vkId, events)
        }

    suspend fun updateInfo(vkId: Long, info: UserInfo) =
        withContext(dispatcher) {
            clientsRepository.updateInfo(vkId, info)
        }

    suspend fun updateFavFriends(vkId: Long, friendsIds: List<Long>) =
        withContext(dispatcher) {
            clientsRepository.updateFavFriends(vkId, friendsIds)
        }

    suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>) =
        withContext(dispatcher) {
            clientsRepository.updateWishlists(vkId, wishlists)
        }
}
