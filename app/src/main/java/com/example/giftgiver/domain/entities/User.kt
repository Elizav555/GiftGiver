package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val vkId: Long,
    val name: String = "",
    val photo: String = "",
    var info: UserInfo? = null
) : Parcelable
