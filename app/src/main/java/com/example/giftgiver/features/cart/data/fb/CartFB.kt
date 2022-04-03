package com.example.giftgiver.features.cart.data.fb

data class CartFB(
    var giftsIdsAndFor: List<GiftInfo> = listOf(),
)

data class GiftInfo(
    var giftId: String = "",
    var forId: Long = 0
)
