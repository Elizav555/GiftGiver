package com.example.giftgiver.firebase.entities

data class ClientFB(
    val vkId: Long? = 0,
    val calendar: CalendarFB,
    val cart: CartFB,
    val favFriends: List<UserFB> = listOf(),
)
