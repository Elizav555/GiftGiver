package com.example.giftgiver.data.firebase

import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.google.firebase.firestore.FirebaseFirestore

const val CLIENTS = "clients"

class ClientsRepositoryImpl : ClientsRepository {
    private val clients = FirebaseFirestore.getInstance().collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        val clientFB = FBMapper().mapClientToFB(client)
        clients.document(client.vkId.toString()).set(clientFB)
        //clients.document(client.vkId.toString()).collection("wishlists").add(clientFB.wishlists)
    }

    //    override suspend fun addClient(client: Client) {
//        val clientFB = FBMapper().mapClientToFB(client)
//        val map = ClientFB::class.declaredMemberProperties.associate { it.name to it.get(clientFB) }
//        map.forEach { prop ->
//            prop.value?.let {
//                clients.document(client.vkId.toString()).collection(prop.key).add(
//                    it
//                )
//            }
//        }
//    }
    override suspend fun deleteClient(client: Client) =
        clients.document(client.vkId.toString()).delete()

    override suspend fun getClientByVkId(vkId: Long) = clients.document(vkId.toString()).get()
    override suspend fun updateClient(vkId: Long, changes: Map<String, Any>) {
        clients.document(vkId.toString())
            .update(changes)
    }
}
