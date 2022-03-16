package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.firebase.entities.EventFB
import com.example.giftgiver.data.firebase.entities.GiftFB
import com.example.giftgiver.data.firebase.entities.WishlistFB
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FBMapper {
    fun mapClientToFB(client: Client): ClientFB {
        val clientFB = ClientFB(client.vkId)
        clientFB.calendar.events = client.calendar.events.map { mapEventToFB(it) }
        clientFB.cart.gifts = client.cart.gifts.map { mapGiftToFB(it) }
        clientFB.favFriendsIds = client.favFriends.map { it.vkId } as MutableList<Long>
        clientFB.wishlists =
            client.wishlists.map { mapWishlistToFB(it) } as MutableList<WishlistFB>
        return clientFB
    }

    private fun mapWishlistToFB(wishlist: Wishlist) = WishlistFB(
        name = wishlist.name,
        gifts = wishlist.gifts.map { mapGiftToFB(it) },
    )

    private fun mapWishlistFromFB(wishlist: WishlistFB) = Wishlist(
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
        client.cart.gifts = clientFB.cart.gifts.map { mapGiftFromFB(it) } as MutableList<Gift>
        client.favFriends =
            clientFB.favFriendsIds.map {
                withContext(Dispatchers.Default) {
                    LoadUserInfoVK().loadInfo(
                        it
                    )
                }
            }
        client.wishlists =
            clientFB.wishlists.map { mapWishlistFromFB(it) } as MutableList<Wishlist>
        return client
    }
}
