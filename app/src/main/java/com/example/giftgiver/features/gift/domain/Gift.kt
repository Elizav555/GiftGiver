package com.example.giftgiver.features.gift.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Gift(
    val id: String,
    var name: String,
    val forId: Long = 0,
    val forName: String? = null,
    var desc: String?,
    var imageUrl: String?,
    var isChosen: Boolean = false,
    var lastChanged: Calendar,
    val wishlistIndex: Int
) : Parcelable
