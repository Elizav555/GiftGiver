package com.example.giftgiver.data.firebase.entities

data class ClientFB(
    val vkId: Long? = 0,
    val calendar: CalendarFB = CalendarFB(),
    val cart: CartFB = CartFB(),
    var favFriends: List<UserFB> = listOf(),
)
