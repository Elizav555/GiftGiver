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

class RoomMapper {
    val numberFormat = NumberFormat.getInstance()
    fun mapClientToRoom(client: Client): ClientR {
        return ClientR(
            vkId = client.vkId,
            favFriendsIds = client.favFriendsIds.map { numberFormat.format(it) },
            info = mapUserInfoToRoom(client.info),
            events = client.calendar.events.map { mapEventToRoom(it) },
            giftsInfo = client.cart.giftsInfo.map { mapGiftInfoToRoom(it) },
            wishlists = client.wishlists.map { mapWishlistToRoom(it) }
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

    private fun mapWishlistToRoom(wishlist: Wishlist): WishlistR {
        return WishlistR(
            name = wishlist.name,
            giftsIds = wishlist.giftsIds
        )
    }

    private fun mapGiftInfoToRoom(giftInfo: GiftInfo) = GiftInfoR(
        giftId = giftInfo.giftId,
        forId = giftInfo.forId,
        lastSeen = giftInfo.lastSeen.time
    )

    private fun mapEventToRoom(event: Event): EventR {
        return EventR(
            date = event.date.time,
            desc = event.desc
        )
    }

    fun mapClientFromRoom(
        client: ClientR,
    ): Client {
        return Client(
            vkId = client.vkId,
            calendar = Calendar(client.events.map { mapEventFromRoom(it) } as MutableList<Event>),
            cart = Cart(client.giftsInfo.map { mapGiftInfoFromRoom(it) } as MutableList<GiftInfo>),
            favFriendsIds = client.favFriendsIds.map {
                numberFormat.parse(it)
            } as? MutableList<Long> ?: mutableListOf(),
            wishlists = client.wishlists.map { mapWishlistFromRoom(it) } as MutableList<Wishlist>,
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

    private fun mapWishlistFromRoom(wishlist: WishlistR): Wishlist {
        return Wishlist(
            name = wishlist.name,
            giftsIds = wishlist.giftsIds as MutableList<String>
        )
    }

    private fun mapGiftInfoFromRoom(giftInfo: GiftInfoR): GiftInfo {
        val calendar = java.util.Calendar.getInstance()
        calendar.time = giftInfo.lastSeen
        return GiftInfo(
            giftId = giftInfo.giftId,
            forId = giftInfo.forId,
            lastSeen = calendar
        )
    }

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
