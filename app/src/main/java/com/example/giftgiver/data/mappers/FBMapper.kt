package com.example.giftgiver.data.mappers

import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.firebase.entities.EventFB
import com.example.giftgiver.data.firebase.entities.GiftFB
import com.example.giftgiver.data.firebase.entities.UserFB
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.entities.User

class FBMapper {
    fun mapUserToFB(user: User): UserFB = UserFB(
        vkId = user.vkId,
        wishlists = user.info?.wishlists ?: mutableListOf()
    )

//    private fun mapUserFromFB(userFB: UserFB): User {
//          LoadUserInfoVK().loadInfo(userFB.vkId,::setUser)
//        todo как синхронно выполнять запрос на получение данных
//        User(
//            vkId = userFB.vkId,
//            name = "",
//            photo = "",
//            info = null
//        )
//    }

    fun mapClientToFB(client: Client): ClientFB {
        val clientFB = ClientFB(client.vkId)
        clientFB.calendar.events = client.calendar.events.map { mapEventToFB(it) }
        clientFB.cart.gifts = client.cart.gifts.map { mapGiftToFB(it) }
        clientFB.favFriends = client.favFriends.map { mapUserToFB(it) }
        return clientFB
    }

    fun mapEventToFB(event: Event): EventFB =
        EventFB(name = event.name, date = event.date, desc = event.desc)

    fun mapGiftToFB(gift: Gift): GiftFB = GiftFB(
        name = gift.name,
        forUser = gift.forUser,
        desc = gift.desc,
        imageUrl = gift.imageUrl,
        isChosen = gift.isChosen
    )

//    private fun mapClientFromFB(clientFB: UserFB): User {
//          LoadUserInfoVK().loadInfo(userFB.vkId,::setUser)
//        todo как синхронно выполнять запрос на получение данных
//        User(
//            vkId = clientFB.vkId,
//            name = "",
//            photo = "",
//            info = null
//        )
//    }
}