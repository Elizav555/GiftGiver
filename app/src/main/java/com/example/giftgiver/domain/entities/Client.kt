package com.example.giftgiver.domain.entities

data class Client(
    val vkId: Long = 0,
    val calendar: Calendar = Calendar(),
    val user: User,
    val cart: Cart = Cart(),
    var favFriends: List<User> = listOf(),
)
