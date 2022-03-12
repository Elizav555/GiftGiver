package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.firebase.entities.*
import com.example.giftgiver.domain.entities.*
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FBMapper {
    private fun mapUserToFB(user: User): UserFB = UserFB(
        vkId = user.vkId,
        wishlists = user.info.wishlists
    )

    private suspend fun mapUserFromFB(userFB: UserFB): User {
        return withContext(Dispatchers.Default) {
            LoadUserInfoVK().loadInfo(userFB.vkId)
        }
    }

    fun mapClientToFB(client: Client): ClientFB {
        val clientFB = ClientFB(client.vkId)
        clientFB.calendar.events = client.calendar.events.map { mapEventToFB(it) }
        clientFB.cart.gifts = client.cart.gifts.map { mapGiftToFB(it) }
        clientFB.favFriends = client.favFriends.map { mapUserToFB(it) }
        clientFB.wishlists =
            client.user.info.wishlists.map { mapWishlistToFB(it) } as MutableList<WishlistFB>
        return clientFB
    }

    fun mapWishlistToFB(wishlist: Wishlist) = WishlistFB(
        name = wishlist.name,
        gifts = wishlist.gifts.map { mapGiftToFB(it) },
    )

    fun mapWishlistFromFB(wishlist: WishlistFB) = Wishlist(
        name = wishlist.name,
        gifts = wishlist.gifts.map { mapGiftFromFB(it) } as MutableList<Gift>,
    )

    private fun mapEventToFB(event: Event): EventFB =
        EventFB(name = event.name, date = event.date, desc = event.desc)

    private fun mapGiftToFB(gift: Gift): GiftFB = GiftFB(
        name = gift.name,
        forUser = gift.forUser,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen
    )

    private fun mapEventFromFB(event: EventFB): Event =
        Event(name = event.name, date = event.date, desc = event.desc)

    private fun mapGiftFromFB(gift: GiftFB): Gift = Gift(
        name = gift.name,
        forUser = gift.forUser,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen
    )

    suspend fun mapClientFromFB(clientFB: ClientFB): Client {
        val user = withContext(Dispatchers.Default) { LoadUserInfoVK().loadInfo(clientFB.vkId) }
        val client = Client(clientFB.vkId, user = user)
        client.calendar.events = clientFB.calendar.events.map { mapEventFromFB(it) }
        client.cart.gifts = clientFB.cart.gifts.map { mapGiftFromFB(it) }
        client.favFriends =
            clientFB.favFriends.map { withContext(Dispatchers.Default) { mapUserFromFB(it) } }
        client.user.info.wishlists =
            clientFB.wishlists.map { mapWishlistFromFB(it) } as MutableList<Wishlist>
        return client
    }
}
