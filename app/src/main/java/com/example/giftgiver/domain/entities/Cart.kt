package com.example.giftgiver.domain.entities

data class Cart(
    var gifts: MutableList<Gift> = mutableListOf(),
)
