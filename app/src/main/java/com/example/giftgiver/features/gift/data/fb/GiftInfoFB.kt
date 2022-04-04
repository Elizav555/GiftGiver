package com.example.giftgiver.features.gift.data.fb

import java.util.*

data class GiftInfoFB(
    var giftId: String = "",
    var forId: Long = 0,
    var lastSeen: Date = Date(),
)
