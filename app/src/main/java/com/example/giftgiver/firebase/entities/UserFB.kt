package com.example.giftgiver.firebase.entities

data class UserFB(
    val vkId: Long? = 0,
    val wishlists: List<WishlistFB> = listOf(),
)
