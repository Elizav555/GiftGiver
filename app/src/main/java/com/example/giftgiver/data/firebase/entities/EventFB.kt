package com.example.giftgiver.data.firebase.entities

import java.util.*

data class EventFB(
    val name: String = "",
    val date: Calendar = Calendar.getInstance(),
    val desc: String?,
)
