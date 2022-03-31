package com.example.giftgiver.features.calendar.domain

import android.os.Parcelable
import com.example.giftgiver.features.event.domain.Event
import kotlinx.parcelize.Parcelize

@Parcelize
data class Calendar(
    var events: MutableList<Event> = mutableListOf(),
) : Parcelable
