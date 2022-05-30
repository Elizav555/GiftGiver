package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.utils.AppDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClientFBInteractor @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: AppDispatchers,
) {
    suspend fun updateCart(vkId: Long, giftsIds: List<GiftInfo>) =
        withContext(dispatcher.io) {
            clientsRepository.updateCart(vkId, giftsIds)
        }

    suspend fun updateCalendar(vkId: Long, events: List<Event>) =
        withContext(dispatcher.io) {
            clientsRepository.updateCalendar(vkId, events)
        }

    suspend fun updateInfo(vkId: Long, info: UserInfo) =
        withContext(dispatcher.io) {
            clientsRepository.updateInfo(vkId, info)
        }

    suspend fun updateFavFriends(vkId: Long, friendsIds: List<Long>) =
        withContext(dispatcher.io) {
            clientsRepository.updateFavFriends(vkId, friendsIds)
        }

    suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>) =
        withContext(dispatcher.io) {
            clientsRepository.updateWishlists(vkId, wishlists)
        }
}
