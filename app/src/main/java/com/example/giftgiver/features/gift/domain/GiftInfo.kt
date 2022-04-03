package com.example.giftgiver.features.gift.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftInfo(
    var giftId: String = "",
    var forId: Long = 0
) : Parcelable
