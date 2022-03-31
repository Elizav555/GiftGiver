package com.example.giftgiver.features.event.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class Event(
    val date: Calendar,
    val desc: String? = "",
) : Parcelable
