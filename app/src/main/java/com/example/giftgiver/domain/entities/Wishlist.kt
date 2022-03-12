package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wishlist(
    val name: String,
    val gifts: MutableList<Gift> = mutableListOf(),
) : Parcelable
