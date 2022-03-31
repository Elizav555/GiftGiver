package com.example.giftgiver.features.wishlist.domain

import android.os.Parcelable
import com.example.giftgiver.features.gift.domain.Gift
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wishlist(
    var name: String,
    val gifts: MutableList<Gift> = mutableListOf(),
) : Parcelable
