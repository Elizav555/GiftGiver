package com.example.giftgiver.features.event.data.room

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class EventR(
    val date: Date = Date(),
    val desc: String? = "",
) : Parcelable

