package com.example.giftgiver.data.firebase

import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val CLIENTS = "clients"

class ClientsRepositoryImpl(private val fbMapper: FBMapper) : ClientsRepository {
    private val clients = FirebaseFirestore.getInstance().collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        val clientFB = fbMapper.mapClientToFB(client)
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

    override suspend fun getClientByVkId(vkId: Long): Client? {
        val snapshot = getDataSnapshot(vkId)
        val clientFB = snapshot.toObject<ClientFB>()
        return clientFB?.let { fbMapper.mapClientFromFB(it) }
    }

    private suspend fun getDataSnapshot(vkId: Long): DocumentSnapshot {
        return suspendCoroutine { continuation ->
            clients.document(vkId.toString()).get().addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }

    override suspend fun updateClient(vkId: Long, changes: Map<String, Any>) {
        clients.document(vkId.toString())
            .update(changes)
    }
//
//    override suspend fun updateWishlists(vkId: Long, wishlist: Wishlist, wishlistIndex: Int) {
//        clients.document(vkId.toString()).collection("info").document("wishlists")
//            .update(mapOf(wishlistIndex.toString() to wishlist))
//    }
//
//    override suspend fun updateCart(vkId: Long, wishlist: Wishlist, wishlistIndex: Int) {
//        clients.document(vkId.toString()).collection("info").document("wishlists")
//            .update(mapOf(wishlistIndex.toString() to wishlist))
//    }
}
