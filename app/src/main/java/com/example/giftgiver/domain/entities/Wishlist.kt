package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wishlist(
    var id: Long,
    val name: String,
    val gifts: List<Gift> = listOf(),
) : Parcelable
