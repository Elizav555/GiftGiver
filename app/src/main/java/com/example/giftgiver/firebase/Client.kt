package com.example.giftgiver.firebase

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Client(
    var vkId: Long? = 0,
    var addInfo: String? = "",
    var wishlists: List<String>? = listOf(),
)
