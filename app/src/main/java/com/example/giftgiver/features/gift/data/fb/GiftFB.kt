package com.example.giftgiver.features.gift.data.fb

import java.util.*

data class GiftFB(
    val name: String = "",
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String? = "",
    val imageUrl: String? = "",
    var isChosen: Boolean = false,
    var lastChanged: Date = Date(),
    val wishlistIndex: Int = 0
)
