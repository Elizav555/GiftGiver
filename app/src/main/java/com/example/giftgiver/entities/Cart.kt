package com.example.giftgiver.entities

data class Cart(
    val id: Long,
    val gifts: List<Gift> = listOf(),
)
