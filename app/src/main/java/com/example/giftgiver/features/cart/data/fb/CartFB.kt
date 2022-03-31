package com.example.giftgiver.features.cart.data.fb

import com.example.giftgiver.features.gift.data.fb.GiftFB

data class CartFB(
    var gifts: List<GiftFB> = listOf(),
)
