package com.example.giftgiver.features.client.domain.repositories

import androidx.lifecycle.LiveData
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist

interface ClientsRepository {
    suspend fun addClient(client: Client)
    suspend fun deleteClient(client: Client)
    suspend fun getClientByVkId(vkId: Long): Client?
    suspend fun updateClient(vkId: Long, changes: Map<String, Any>)
    suspend fun updateInfo(vkId: Long, info: UserInfo)
    suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>)
    suspend fun updateCart(vkId: Long, giftsIds: List<GiftInfo>)
    suspend fun updateCalendar(vkId: Long, events: List<Event>)
    suspend fun updateFavFriends(vkId: Long, friendsIds: List<Long>)
    fun isClientChanged(): LiveData<Boolean>
}
