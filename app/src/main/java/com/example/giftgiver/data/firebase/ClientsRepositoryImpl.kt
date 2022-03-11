package com.example.giftgiver.data.firebase

import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.full.declaredMemberProperties

class ClientsRepositoryImpl : ClientsRepository {
    private val clients = FirebaseFirestore.getInstance().collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        val map = ClientFB::class.declaredMemberProperties.associate {
            it.name to it.get(
                FBMapper().mapClientToFB(client)
            )
        }
        clients.add(map)
    }

    override suspend fun deleteClient(client: Client) =
        clients.document(client.vkId.toString()).delete()

    override suspend fun getClientByVkId(vkId: Long) = clients.whereEqualTo("vkId", vkId).get()
}
