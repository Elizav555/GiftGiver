package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Event(
    val name: String,
    val date: Date,
    val desc: String?,
) : Parcelable
