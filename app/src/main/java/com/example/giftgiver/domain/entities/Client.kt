package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val vkId: Long = 0,
    val calendar: Calendar = Calendar(),
    var user: User,
    val cart: Cart = Cart(),
    var favFriends: MutableList<User> = mutableListOf(),
    var wishlists: MutableList<Wishlist> = mutableListOf(),
) : Parcelable
