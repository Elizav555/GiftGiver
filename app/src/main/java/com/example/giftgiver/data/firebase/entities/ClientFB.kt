package com.example.giftgiver.data.firebase.entities

data class ClientFB(
    val vkId: Long? = 0,
    val calendar: CalendarFB = CalendarFB(),
    val cart: CartFB = CartFB(),
    val favFriends: List<UserFB> = listOf(),
)
