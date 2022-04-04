package com.example.giftgiver.features.cart.data.fb

import com.example.giftgiver.features.gift.data.fb.GiftInfoFB

data class CartFB(
    var giftsInfo: List<GiftInfoFB> = listOf(),
)
