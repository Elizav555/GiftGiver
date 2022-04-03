package com.example.giftgiver.features.gift.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gift(
    val id: String,
    var name: String,
    val forId: Long = 0,
    val forName: String? = null,
    var desc: String?,
    var imageUrl: String?,
    var isChosen: Boolean = false,
    var isChanged: Boolean = false,
    val wishlistIndex: Int
) : Parcelable
