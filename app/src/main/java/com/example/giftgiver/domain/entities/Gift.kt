package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gift(
    val name: String,
    val forUser: Long,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean = false,
) : Parcelable
