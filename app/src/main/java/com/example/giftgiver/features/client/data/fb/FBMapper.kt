package com.example.giftgiver.features.client.data.fb

import com.example.giftgiver.features.calendar.data.fb.CalendarFB
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.data.fb.CartFB
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.event.data.fb.EventFB
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.data.fb.GiftFB
import com.example.giftgiver.features.gift.data.fb.GiftInfoFB
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.data.fb.UserInfoFB
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVK
import com.example.giftgiver.features.wishlist.data.fb.WishlistFB
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.utils.AppDispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar as JavaUtilCalendar

class FBMapper(
    private val dispatcher: AppDispatchers,
    private val loadUserInfoVK: LoadUserInfoVK,
) {
    fun mapClientToFB(client: Client) = with(client) {
        return@with ClientFB(
            vkId,
            calendar = CalendarFB(events = calendar.events.map { mapEventToFB(it) }),
            cart = CartFB(giftsInfo =
            cart.giftsInfo.map { GiftInfoFB(it.giftId, it.forId, it.lastSeen.time) }),
            favFriendsIds = favFriendsIds,
            wishlists = wishlists.map { mapWishlistToFB(it) } as MutableList<WishlistFB>,
            info = with(info) {
                UserInfoFB(
                    name,
                    photo,
                    bdate,
                    about,
                )
            },
            pushToken = client.pushToken
        )
    }

    fun mapWishlistToFB(wishlist: Wishlist) = with(wishlist) {
        WishlistFB(
            name = name,
            giftsIds = giftsIds
        )
    }

    private fun mapWishlistFromFB(wishlist: WishlistFB) = with(wishlist) {
        Wishlist(
            name = name,
            giftsIds = giftsIds as MutableList<String>
        )
    }

    fun mapEventToFB(event: Event): EventFB = with(event) {
        EventFB(
            date = date.time,
            desc = desc
        )
    }

    fun mapGiftToFB(gift: Gift): GiftFB = with(gift) {
        GiftFB(
            name = name,
            forId = forId,
            forName = forName,
            desc = desc,
            imageUrl = imageUrl,
            isChosen = isChosen,
            lastChanged = lastChanged.time,
            wishlistIndex = wishlistIndex
        )
    }

    private fun mapEventFromFB(event: EventFB) = with(event) {
        val calendar = JavaUtilCalendar.getInstance()
        calendar.time = date
        return@with Event(date = calendar, desc = desc)
    }

    fun mapGiftFromFB(gift: GiftFB, giftId: String) = with(gift) {
        val calendar = JavaUtilCalendar.getInstance()
        calendar.time = lastChanged
        return@with Gift(
            id = giftId,
            name = name,
            forId = forId,
            forName = forName,
            desc = desc,
            imageUrl = imageUrl,
            isChosen = isChosen,
            lastChanged = calendar,
            wishlistIndex = wishlistIndex
        )
    }

    suspend fun mapClientFromFB(clientFB: ClientFB) = with(clientFB) {
        val info = info?.let { infoFB ->
            with(infoFB) {
                UserInfo(
                    vkId,
                    name,
                    photo,
                    bdate,
                    about,
                )
            }
        } ?: withContext(dispatcher.io) { loadUserInfoVK.loadInfo(vkId) }
        val calendar = JavaUtilCalendar.getInstance()
        return@with Client(
            vkId,
            info = info,
            wishlists = wishlists.map { mapWishlistFromFB(it) } as MutableList<Wishlist>,
            calendar = Calendar(
                events = clientFB.calendar.events.map { mapEventFromFB(it) } as MutableList<Event>
            ),
            cart = Cart(
                giftsInfo = cart.giftsInfo.map {
                    calendar.time = it.lastSeen
                    GiftInfo(
                        it.giftId,
                        it.forId,
                        calendar
                    )
                } as MutableList<GiftInfo>
            ),
            favFriendsIds = clientFB.favFriendsIds,
            pushToken = clientFB.pushToken
        )
    }

    fun mapGiftsInfoToFB(giftsIds: List<GiftInfo>): List<GiftInfoFB> {
        return giftsIds.map { GiftInfoFB(it.giftId, it.forId, it.lastSeen.time) }
    }
}
