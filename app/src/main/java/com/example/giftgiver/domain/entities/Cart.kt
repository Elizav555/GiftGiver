package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    var gifts: MutableList<Gift> = mutableListOf(),
): Parcelable
