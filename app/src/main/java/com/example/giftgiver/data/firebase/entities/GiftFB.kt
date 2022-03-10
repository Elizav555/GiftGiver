package com.example.giftgiver.data.firebase.entities

data class GiftFB(
    val name: String,
    val forUser: Long,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean = false,
)
