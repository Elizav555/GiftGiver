package com.example.giftgiver.data.firebase.entities

import com.example.giftgiver.domain.entities.Wishlist

data class UserFB(
    val vkId: Long,
    val wishlists: MutableList<Wishlist> = mutableListOf(),
)
