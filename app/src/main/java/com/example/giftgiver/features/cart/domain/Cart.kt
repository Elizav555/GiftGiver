package com.example.giftgiver.features.cart.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    var giftsIdsAndFor: MutableList<Pair<String, Long>> = mutableListOf(),
) : Parcelable
