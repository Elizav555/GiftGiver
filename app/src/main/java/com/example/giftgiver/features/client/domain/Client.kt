package com.example.giftgiver.features.client.domain

import android.os.Parcelable
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val vkId: Long = 0,
    val calendar: Calendar = Calendar(),
    val cart: Cart = Cart(),
    var favFriendsIds: MutableList<Long> = mutableListOf(),
    var wishlists: MutableList<Wishlist> = mutableListOf(),
    var info: UserInfo,
) : Parcelable
