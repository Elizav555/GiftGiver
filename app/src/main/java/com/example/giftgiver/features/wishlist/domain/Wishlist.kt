package com.example.giftgiver.features.wishlist.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wishlist(
    var name: String,
    var giftsIds: MutableList<String> = mutableListOf(),
) : Parcelable
