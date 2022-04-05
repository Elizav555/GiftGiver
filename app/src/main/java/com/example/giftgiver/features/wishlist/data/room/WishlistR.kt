package com.example.giftgiver.features.wishlist.data.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishlistR(
    val name: String = "",
    var giftsIds: List<String> = listOf(),
) : Parcelable
