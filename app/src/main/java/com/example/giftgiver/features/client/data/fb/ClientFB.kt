package com.example.giftgiver.features.client.data.fb

import com.example.giftgiver.features.calendar.data.fb.CalendarFB
import com.example.giftgiver.features.cart.data.fb.CartFB
import com.example.giftgiver.features.user.data.fb.UserInfoFB
import com.example.giftgiver.features.wishlist.data.fb.WishlistFB

data class ClientFB(
    val vkId: Long = 0,
    val calendar: CalendarFB = CalendarFB(),
    val cart: CartFB = CartFB(),
    var favFriendsIds: MutableList<Long> = mutableListOf(),
    var wishlists: MutableList<WishlistFB> = mutableListOf(),
    var info: UserInfoFB? = null,
    var pushToken: String? = null
)
