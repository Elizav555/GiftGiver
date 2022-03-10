package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val vkId: Long = 0,
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
    val wishlists: List<Wishlist> = listOf(),
) : Parcelable
