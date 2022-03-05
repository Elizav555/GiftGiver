package com.example.giftgiver.entities

import java.util.*

data class Event(
    val id: Long,
    val name: String,
    val date: Date,
    val desc: String?,
)
