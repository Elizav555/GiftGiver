package com.example.giftgiver.features.gift.data.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GiftInfoR(
    val cartId: Long = 0,
    var giftId: String = "",
    var forId: Long = 0,
    var lastSeen: Date = Date(),
) : Parcelable
