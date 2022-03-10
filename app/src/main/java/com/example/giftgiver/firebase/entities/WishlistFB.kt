package com.example.giftgiver.firebase.entities

data class WishlistFB(
    val name: String,
    val gifts: List<GiftFB> = listOf(),
)
