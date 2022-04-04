package com.example.giftgiver.features.cart.domain

import android.os.Parcelable
import com.example.giftgiver.features.gift.domain.GiftInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    var giftsInfo: MutableList<GiftInfo> = mutableListOf(),
) : Parcelable
