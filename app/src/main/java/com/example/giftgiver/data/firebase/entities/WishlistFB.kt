package com.example.giftgiver.data.firebase.entities

data class WishlistFB(
    val name: String = "",
    val gifts: List<GiftFB> = listOf(),
)
