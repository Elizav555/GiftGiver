package com.example.giftgiver.common.db.room

import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.client.data.room.ClientR
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.gift.data.room.GiftR
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.data.room.UserInfoR
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.data.room.WishlistR
import com.example.giftgiver.features.wishlist.domain.Wishlist
import java.text.NumberFormat
import  java.util.Calendar as JavaCalendar

class RoomMapper {
    private val numberFormat: NumberFormat = NumberFormat.getInstance()
    fun mapClientToRoom(client: Client): ClientR {
        return with(client) {
            ClientR(
                vkId = vkId,
                favFriendsIds = favFriendsIds.map { numberFormat.format(it) },
                info = mapUserInfoToRoom(info),
                events = calendar.events.map { mapEventToRoom(it) },
                giftsInfo = cart.giftsInfo.map { mapGiftInfoToRoom(it) },
                wishlists = wishlists.map { mapWishlistToRoom(it) }
            )
        }
    }

    private fun mapUserInfoToRoom(info: UserInfo): UserInfoR {
        return with(info) {
            UserInfoR(
                name = name,
                photo = photo,
                bdate = bdate,
                about = about
            )
        }
    }

    private fun mapWishlistToRoom(wishlist: Wishlist): WishlistR {
        return with(wishlist) {
            WishlistR(
                name = name,
                giftsIds = giftsIds
            )
        }
    }

    private fun mapGiftInfoToRoom(giftInfo: GiftInfo) = with(giftInfo) {
        GiftInfoR(
            giftId = giftId,
            forId = forId,
            lastSeen = lastSeen.time
        )
    }

    private fun mapEventToRoom(event: Event): EventR {
        return with(event) {
            EventR(
                date = date.time,
                desc = desc
            )
        }
    }

    fun mapClientFromRoom(
        client: ClientR,
    ) = with(client) {
        Client(
            vkId = vkId,
            calendar = Calendar(events.map { mapEventFromRoom(it) } as MutableList<Event>),
            cart = Cart(giftsInfo.map { mapGiftInfoFromRoom(it) } as MutableList<GiftInfo>),
            favFriendsIds = favFriendsIds.map {
                numberFormat.parse(it)
            } as? MutableList<Long> ?: mutableListOf(),
            wishlists = wishlists.map { mapWishlistFromRoom(it) } as MutableList<Wishlist>,
            info = mapUserInfoFromRoom(info, vkId)
        )
    }

    private fun mapUserInfoFromRoom(info: UserInfoR, vkId: Long) = with(info) {
        UserInfo(
            vkId = vkId,
            name = name,
            photo = photo,
            bdate = bdate,
            about = about
        )
    }

    private fun mapWishlistFromRoom(wishlist: WishlistR) = with(wishlist) {
        Wishlist(
            name = name,
            giftsIds = giftsIds as MutableList<String>
        )
    }

    private fun mapGiftInfoFromRoom(giftInfo: GiftInfoR) = with(giftInfo) {
        val calendar = JavaCalendar.getInstance()
        calendar.time = lastSeen
        return@with GiftInfo(
            giftId = giftId,
            forId = forId,
            lastSeen = calendar
        )
    }

    private fun mapEventFromRoom(event: EventR) = with(event) {
        val calendar = JavaCalendar.getInstance()
        calendar.time = date
        return@with Event(
            date = calendar,
            desc = desc
        )
    }

    fun mapGiftToRoom(gift: Gift) = with(gift) {
        GiftR(
            id = id,
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

    fun mapGiftFromRoom(gift: GiftR) = with(gift) {
        val calendar = JavaCalendar.getInstance()
        lastChanged?.let { calendar.time = it }
        return@with Gift(
            id = id,
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
}
