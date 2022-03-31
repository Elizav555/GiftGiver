package com.example.giftgiver.features.gift.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gift(
    val name: String,
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String?,
    var imageUrl: String?,
    var isChosen: Boolean = false,
) : Parcelable
