package com.example.giftgiver.features.user.data.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoR(
    val name: String = "",
    val photo: String = "",
    val bdate: String? = "",
    val about: String? = "",
) : Parcelable
