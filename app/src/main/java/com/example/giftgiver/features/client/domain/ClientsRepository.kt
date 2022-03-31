package com.example.giftgiver.features.client.domain

import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface ClientsRepository {
    suspend fun addClient(client: Client)
    suspend fun deleteClient(client: Client): Task<Void>
    suspend fun getClientByVkId(vkId: Long): Client?
    suspend fun updateClient(vkId: Long, changes: Map<String, Any>)
    suspend fun getDataSnapshot(vkId: Long): DocumentSnapshot
    suspend fun updateInfo(vkId: Long, info: UserInfo)
    suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>)
    suspend fun updateCart(vkId: Long, gifts: List<Gift>)
    suspend fun updateCalendar(vkId: Long, events: List<Event>)
    suspend fun updateFavFriends(vkId: Long, friends: List<Client>)
}
