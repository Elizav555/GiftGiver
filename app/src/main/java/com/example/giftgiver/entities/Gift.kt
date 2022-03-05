package com.example.giftgiver.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gift(
    val id: Long,
    val name: String,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean = false,
) : Parcelable
