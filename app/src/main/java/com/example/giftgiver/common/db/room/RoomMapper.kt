package com.example.giftgiver.common.db.room

import com.example.giftgiver.features.calendar.data.room.CalendarR
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.data.room.CartR
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

class RoomMapper {
    fun mapClientToRoom(client: Client): ClientR {
        return ClientR(
            vkId = client.vkId,
            favFriendsIds = client.favFriendsIds,
            info = mapUserInfoToRoom(client.info)
        )
    }

    private fun mapUserInfoToRoom(info: UserInfo): UserInfoR {
        return UserInfoR(
            name = info.name,
            photo = info.photo,
            bdate = info.bdate,
            about = info.about
        )
    }

    fun mapWishlistToRoom(wishlist: Wishlist, vkId: Long): WishlistR {
        return WishlistR(
            clientId = vkId,
            name = wishlist.name,
            giftsIds = wishlist.giftsIds
        )
    }

    fun mapCartToRoom(cart: Cart, vkId: Long): CartR =
        CartR(clientId = vkId, giftsInfo = cart.giftsInfo.map { mapGiftInfoToRoom(it) })

    private fun mapGiftInfoToRoom(giftInfo: GiftInfo) = GiftInfoR(
        giftId = giftInfo.giftId,
        forId = giftInfo.forId,
        lastSeen = giftInfo.lastSeen.time
    )

    fun mapCalendarToRoom(calendar: Calendar, vkId: Long): CalendarR =
        CalendarR(clientId = vkId, events = calendar.events.map { mapEventToRoom(it) })

    private fun mapEventToRoom(event: Event): EventR {
        return EventR(
            date = event.date.time,
            desc = event.desc
        )
    }

    fun mapClientFromRoom(
        client: ClientR,
        wishlists: List<Wishlist>,
        cart: Cart,
        calendar: Calendar
    ): Client {
        return Client(
            vkId = client.vkId,
            calendar = calendar,
            cart = cart,
            favFriendsIds = client.favFriendsIds as MutableList<Long>,
            wishlists = wishlists as MutableList<Wishlist>,
            info = mapUserInfoFromRoom(client.info, client.vkId)
        )
    }

    private fun mapUserInfoFromRoom(info: UserInfoR, vkId: Long): UserInfo {
        return UserInfo(
            vkId = vkId,
            name = info.name,
            photo = info.photo,
            bdate = info.bdate,
            about = info.about
        )
    }

    fun mapWishlistFromRoom(wishlist: WishlistR): Wishlist {
        return Wishlist(
            name = wishlist.name,
            giftsIds = wishlist.giftsIds as MutableList<String>
        )
    }

    fun mapCartFromRoom(cart: CartR): Cart =
        Cart(giftsInfo = cart.giftsInfo.map { mapGiftInfoFromRoom(it) } as MutableList<GiftInfo>)

    private fun mapGiftInfoFromRoom(giftInfo: GiftInfoR): GiftInfo {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = giftInfo.lastSeen
        return GiftInfo(
            giftId = giftInfo.giftId,
            forId = giftInfo.forId,
            lastSeen = calendar
        )
    }

    fun mapCalendarFromRoom(calendar: CalendarR): Calendar =
        Calendar(events = calendar.events.map { mapEventFromRoom(it) } as MutableList<Event>)

    private fun mapEventFromRoom(event: EventR): Event {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = event.date
        return Event(
            date = calendar,
            desc = event.desc
        )
    }

    fun mapGiftToRoom(gift: Gift) = GiftR(
        id = gift.id,
        name = gift.name,
        forId = gift.forId,
        forName = gift.forName,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen,
        lastChanged = gift.lastChanged.time,
        wishlistIndex = gift.wishlistIndex
    )

    fun mapGiftFromRoom(gift: GiftR): Gift {
        val calendar = java.util.Calendar.getInstance()
        gift.lastChanged?.let { calendar.time = it }
        return Gift(
            id = gift.id,
            name = gift.name,
            forId = gift.forId,
            forName = gift.forName,
            desc = gift.desc,
            imageUrl = gift.imageUrl,
            isChosen = gift.isChosen,
            lastChanged = calendar,
            wishlistIndex = gift.wishlistIndex
        )
    }
}
