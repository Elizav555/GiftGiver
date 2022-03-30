package com.example.giftgiver.domain.repositories

import com.example.giftgiver.domain.entities.*
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
