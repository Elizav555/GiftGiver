package com.example.giftgiver.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Client(
    val vkId: Long? = 0,
    val calendar: Calendar,
    val user: User,
    val cart: Cart,
    val favFriends: List<User> = listOf(),
)
