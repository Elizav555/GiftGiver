package com.example.giftgiver.data.firebase.entities

data class GiftFB(
    val name: String = "",
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String? = "",
    val imageUrl: String? = "",
    var isChosen: Boolean = false,
)
