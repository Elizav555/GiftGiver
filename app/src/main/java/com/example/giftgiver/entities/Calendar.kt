package com.example.giftgiver.entities

data class Calendar(
    val id: Long,
    val events: List<Event> = listOf(),
)
