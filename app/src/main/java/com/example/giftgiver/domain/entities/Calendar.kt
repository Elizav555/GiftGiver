package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Calendar(
    var events: MutableList<Event> = mutableListOf(),
) : Parcelable
