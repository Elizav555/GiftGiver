package com.example.giftgiver.data.firebase

import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val CLIENTS = "clients"
const val WISHLISTS = "wishlists"
const val CART = "cart"
const val CALENDAR = "calendar"
const val FAV_FRIENDS = "favFriends"
const val EVENTS = "events"
const val GIFTS = "gifts"

class ClientsRepositoryImpl(
    private val fbMapper: FBMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ClientsRepository {
    private val clients = FirebaseFirestore.getInstance().collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        val clientRef = clients.document(client.vkId.toString())
        val clientFB = fbMapper.mapClientToFB(client)
        clientRef.set(clientFB)
//        clientFB.info?.let { clientRef.set(it) }
//        clientFB.wishlists.forEach { clientRef.collection(WISHLISTS).add(it) }
//        clientFB.favFriendsIds.forEach { clientRef.collection(FAV_FRIENDS).add(it) }
//        clientRef.collection(CALENDAR).document(EVENTS).set(clientFB.calendar.events)
//        clientRef.collection(CART).document(GIFTS).set(clientFB.cart.gifts)
    }

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
        clients.document(vkId.toString()).update(changes)
    }

    fun updateWishlists(vkId: Long, wishlists: List<Wishlist>) {
        val wishlistsFB = wishlists.map{ fbMapper.mapWishlistToFB(it) }
        clients.document(vkId.toString()).update("$WISHLISTS", wishlistsFB)
    }

    fun updateCart(vkId: Long, gifts: List<Gift>) {
        val giftsFB = gifts.map { fbMapper.mapGiftToFB(it) }
        clients.document(vkId.toString()).update("$CART/$GIFTS", giftsFB)
    }

    fun updateCalendar(vkId: Long, events: List<Event>) {
        val eventsFB = events.map { fbMapper.mapEventToFB(it) }
        clients.document("$vkId").update("$CALENDAR/$EVENTS", eventsFB)
    }

    fun updateFavFriends(vkId: Long, friends: List<Client>) {
        val friendsFB = friends.map { it.vkId }
        clients.document("$vkId").update(FAV_FRIENDS, friendsFB)
    }
}
