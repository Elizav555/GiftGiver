package com.example.giftgiver.features.wishlist.data.fb

import com.example.giftgiver.features.gift.data.fb.GiftFB

data class WishlistFB(
    val name: String = "",
    val gifts: List<GiftFB> = listOf(),
)
