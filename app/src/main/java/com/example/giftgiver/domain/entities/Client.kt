package com.example.giftgiver.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    val vkId: Long = 0,
    val calendar: Calendar = Calendar(),
    val cart: Cart = Cart(),
    var favFriends: MutableList<Client> = mutableListOf(),
    var info: UserInfo,
) : Parcelable
