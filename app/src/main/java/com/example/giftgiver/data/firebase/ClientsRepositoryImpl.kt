package com.example.giftgiver.data.firebase

import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.google.firebase.firestore.FirebaseFirestore

class ClientsRepositoryImpl : ClientsRepository {
    private val clients = FirebaseFirestore.getInstance().collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        clients.document(client.vkId.toString()).set(FBMapper().mapClientToFB(client))
    }

    override suspend fun deleteClient(client: Client) =
        clients.document(client.vkId.toString()).delete()

    override suspend fun getClientByVkId(vkId: Long) = clients.document(vkId.toString()).get()
    override suspend fun updateClient(vkId: Long, changes: Map<String, Any>) {
        clients.document(vkId.toString())
            .update(changes)
    }
}
