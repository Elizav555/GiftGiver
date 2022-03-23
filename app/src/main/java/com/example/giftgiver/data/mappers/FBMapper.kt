package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.entities.*
import com.example.giftgiver.domain.entities.*
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FBMapper {
    private val clients = ClientsRepositoryImpl(this)
    fun mapClientToFB(client: Client): ClientFB {
        val clientFB = ClientFB(client.vkId)
        clientFB.calendar.events = client.calendar.events.map { mapEventToFB(it) }
        clientFB.cart.gifts = client.cart.gifts.map { mapGiftToFB(it) }
        clientFB.favFriendsIds = client.favFriends.map { it.vkId } as MutableList<Long>
        clientFB.wishlists = client.wishlists.map { mapWishlistToFB(it) } as MutableList<WishlistFB>
        clientFB.info = with(client.info) {
            UserInfoFB(
                name,
                photo,
                bdate,
                about,
                photoMax,
            )
        }
        return clientFB
    }

    fun mapWishlistToFB(wishlist: Wishlist) = WishlistFB(
        name = wishlist.name,
        gifts = wishlist.gifts.map { mapGiftToFB(it) },
    )

    private fun mapWishlistFromFB(wishlist: WishlistFB) = Wishlist(
        name = wishlist.name,
        gifts = wishlist.gifts.map { mapGiftFromFB(it) } as MutableList<Gift>,
    )

    fun mapEventToFB(event: Event): EventFB =
        EventFB(name = event.name, date = event.date, desc = event.desc)

    fun mapGiftToFB(gift: Gift): GiftFB = GiftFB(
        name = gift.name,
        forId = gift.forId,
        forName = gift.forName,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen
    )

    private fun mapEventFromFB(event: EventFB): Event =
        Event(name = event.name, date = event.date, desc = event.desc)

    private fun mapGiftFromFB(gift: GiftFB): Gift = Gift(
        name = gift.name,
        forId = gift.forId,
        forName = gift.forName,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen
    )

    suspend fun mapClientFromFB(clientFB: ClientFB): Client {
        var info = UserInfo(clientFB.vkId)
        if (clientFB.info == null) {
            info = withContext(Dispatchers.Default) { LoadUserInfoVK().loadInfo(clientFB.vkId) }
        } else clientFB.info?.let { infoFB ->
            info = UserInfo(
                clientFB.vkId,
                infoFB.name,
                infoFB.photo,
                infoFB.bdate,
                infoFB.about,
                infoFB.photoMax,
            )
        }
        val client = Client(clientFB.vkId, info = info)
        client.wishlists = clientFB.wishlists.map { mapWishlistFromFB(it) } as MutableList<Wishlist>
        client.calendar.events = clientFB.calendar.events.map { mapEventFromFB(it) }
        client.cart.gifts = clientFB.cart.gifts.map { mapGiftFromFB(it) } as MutableList<Gift>
        val favFriendsFB =
            clientFB.favFriendsIds.map {
                withContext(Dispatchers.Default) {
                    clients.getClientByVkId(it)
                }
            }
        client.favFriends = favFriendsFB.filterNotNull() as MutableList<Client>
        return client
    }
}
