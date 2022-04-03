package com.example.giftgiver.features.gift.data.fb

data class GiftFB(
    val name: String = "",
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String? = "",
    val imageUrl: String? = "",
    var isChosen: Boolean = false,
    var isChanged: Boolean = false,
    val wishlistIndex: Int = 0
)
