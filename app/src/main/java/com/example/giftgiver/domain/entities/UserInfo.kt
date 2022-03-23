package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val vkId: Long,
    val name: String = "",
    val photo: String = "",
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
    var wishlists: MutableList<Wishlist> = mutableListOf(),
) : Parcelable
