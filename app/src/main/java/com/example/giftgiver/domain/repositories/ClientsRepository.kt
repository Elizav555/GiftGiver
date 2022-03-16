package com.example.giftgiver.domain.repositories

import com.example.giftgiver.domain.entities.Client
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface ClientsRepository {
    suspend fun addClient(client: Client)
    suspend fun deleteClient(client: Client): Task<Void>
    suspend fun getClientByVkId(vkId: Long): DocumentSnapshot
    suspend fun updateClient(vkId: Long, changes: Map<String, Any>)
}
