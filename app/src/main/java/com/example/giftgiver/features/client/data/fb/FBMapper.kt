package com.example.giftgiver.features.client.data.fb

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.event.data.fb.EventFB
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.data.fb.GiftFB
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.user.data.fb.UserInfoFB
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVK
import com.example.giftgiver.features.wishlist.data.fb.WishlistFB
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.*

class FBMapper(
    private val dispatcher: CoroutineDispatcher,
    private val loadUserInfoVK: LoadUserInfoVK
) {
    fun mapClientToFB(client: Client): ClientFB {
        val clientFB = ClientFB(client.vkId)
        clientFB.calendar.events = client.calendar.events.map { mapEventToFB(it) }
        clientFB.cart.gifts = client.cart.gifts.map { mapGiftToFB(it) }
        clientFB.favFriendsIds = client.favFriendsIds
        clientFB.wishlists = client.wishlists.map { mapWishlistToFB(it) } as MutableList<WishlistFB>
        clientFB.info = with(client.info) {
            UserInfoFB(
                name,
                photo,
                bdate,
                about,
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
        EventFB(date = event.date.time, desc = event.desc)

    fun mapGiftToFB(gift: Gift): GiftFB = GiftFB(
        name = gift.name,
        forId = gift.forId,
        forName = gift.forName,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen,
        wishlistIndex = gift.wishlistIndex
    )

    private fun mapEventFromFB(event: EventFB): Event {
        val calendar = Calendar.getInstance()
        calendar.time = event.date
        return Event(date = calendar, desc = event.desc)
    }

    private fun mapGiftFromFB(gift: GiftFB): Gift = Gift(
        name = gift.name,
        forId = gift.forId,
        forName = gift.forName,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen,
        wishlistIndex = gift.wishlistIndex
    )

    suspend fun mapClientFromFB(clientFB: ClientFB): Client {
        var info = UserInfo(clientFB.vkId)
        if (clientFB.info == null) {
            info = withContext(dispatcher) { loadUserInfoVK.loadInfo(clientFB.vkId) }
        } else clientFB.info?.let { infoFB ->
            info = UserInfo(
                clientFB.vkId,
                infoFB.name,
                infoFB.photo,
                infoFB.bdate,
                infoFB.about,
            )
        }
        val client = Client(clientFB.vkId, info = info)
        client.wishlists = clientFB.wishlists.map { mapWishlistFromFB(it) } as MutableList<Wishlist>
        client.calendar.events =
            clientFB.calendar.events.map { mapEventFromFB(it) } as MutableList<Event>
        client.cart.gifts = clientFB.cart.gifts.map { mapGiftFromFB(it) } as MutableList<Gift>
        client.favFriendsIds = clientFB.favFriendsIds
        return client
    }
}
