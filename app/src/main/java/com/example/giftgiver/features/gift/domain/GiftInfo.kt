package com.example.giftgiver.features.gift.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class GiftInfo(
    var giftId: String = "",
    var forId: Long = 0,
    var lastSeen: Calendar
) : Parcelable
