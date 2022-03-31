package com.example.giftgiver.features.user.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val vkId: Long,
    var name: String = "",
    var photo: String = "",
    var bdate: String? = null,
    var about: String? = "",
) : Parcelable
