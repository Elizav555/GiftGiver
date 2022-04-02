package com.example.giftgiver.features.client.data.fb

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.ClientsRepository
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.user.data.fb.UserInfoFB
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val CLIENTS = "clients"
const val WISHLISTS = "wishlists"
const val CART = "cart"
const val CALENDAR = "calendar"
const val FAV_FRIENDS = "favFriendsIds"
const val EVENTS = "events"
const val GIFTS = "gifts"
const val INFO = "info"

class ClientsRepositoryImpl(
    private val fbMapper: FBMapper,
    private val firebaseFirestore: FirebaseFirestore
) : ClientsRepository {
    private val clients = firebaseFirestore.collection(CLIENTS)

    override suspend fun addClient(client: Client) {
        val clientRef = clients.document(client.vkId.toString())
        val clientFB = fbMapper.mapClientToFB(client)
        clientRef.set(clientFB)
    }

    override suspend fun deleteClient(client: Client) =
        clients.document(client.vkId.toString()).delete()

    override suspend fun getClientByVkId(vkId: Long): Client? {
        val snapshot = getDataSnapshot(vkId)
        val clientFB = snapshot.toObject<ClientFB>()
        return clientFB?.let { fbMapper.mapClientFromFB(it) }
    }

    override suspend fun getDataSnapshot(vkId: Long): DocumentSnapshot {
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

    override suspend fun updateInfo(vkId: Long, info: UserInfo) {
        val infoFB = UserInfoFB(
            info.name,
            info.photo,
            info.bdate,
            info.about,
        )
        clients.document(vkId.toString()).update(INFO, infoFB)
    }

    override suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>) {
        val wishlistsFB = wishlists.map { fbMapper.mapWishlistToFB(it) }
        clients.document(vkId.toString()).update(WISHLISTS, wishlistsFB)
    }

    override suspend fun updateCart(vkId: Long, gifts: List<Gift>) {
        val giftsFB = gifts.map { fbMapper.mapGiftToFB(it) }
        clients.document(vkId.toString()).update("${FieldPath.of(CART, GIFTS)}", giftsFB)
    }

    override suspend fun updateCalendar(vkId: Long, events: List<Event>) {
        val eventsFB = events.map { fbMapper.mapEventToFB(it) }
        clients.document("$vkId").update("${FieldPath.of(CALENDAR, EVENTS)}", eventsFB)
    }

    override suspend fun updateFavFriends(vkId: Long, friendsIds: List<Long>) {
        clients.document("$vkId").update(FAV_FRIENDS, friendsIds)
    }
}
