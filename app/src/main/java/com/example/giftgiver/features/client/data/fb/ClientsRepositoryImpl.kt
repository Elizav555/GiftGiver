package com.example.giftgiver.features.client.data.fb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.user.data.fb.UserInfoFB
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.vk.api.sdk.VK
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ClientsRepositoryImpl(
    private val fbMapper: FBMapper,
    private val firebaseFirestore: FirebaseFirestore
) : ClientsRepository {
    private val clients = firebaseFirestore.collection(CLIENTS)
    private var curClientChanged = false
    private val isChanged: MutableLiveData<Boolean> = MutableLiveData(curClientChanged)

    init {
        isChanged.postValue(curClientChanged)
    }

    override fun isClientChanged(): LiveData<Boolean> = isChanged

    override suspend fun addClient(client: Client) {
        val clientRef = clients.document(client.vkId.toString())
        val clientFB = fbMapper.mapClientToFB(client)
        clientRef.set(clientFB)
    }

    override suspend fun deleteClient(client: Client) {
        clients.document(client.vkId.toString()).delete()
    }

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
        checkChanges(vkId)
    }

    override suspend fun updateInfo(vkId: Long, info: UserInfo) {
        val infoFB = UserInfoFB(
            info.name,
            info.photo,
            info.bdate,
            info.about,
        )
        clients.document(vkId.toString()).update(INFO, infoFB)
        checkChanges(vkId)
    }

    override suspend fun updateWishlists(vkId: Long, wishlists: List<Wishlist>) {
        val wishlistsFB = wishlists.map { fbMapper.mapWishlistToFB(it) }
        clients.document(vkId.toString()).update(WISHLISTS, wishlistsFB)
        checkChanges(vkId)
    }

    override suspend fun updateCart(vkId: Long, giftsIds: List<GiftInfo>) {
        val giftsIdsFB = fbMapper.mapGiftsInfoToFB(giftsIds)
        clients.document(vkId.toString()).update("${FieldPath.of(CART, GIFTS_IDS)}", giftsIdsFB)
        checkChanges(vkId)
    }

    override suspend fun updateCalendar(vkId: Long, events: List<Event>) {
        val eventsFB = events.map { fbMapper.mapEventToFB(it) }
        clients.document("$vkId").update("${FieldPath.of(CALENDAR, EVENTS)}", eventsFB)
        checkChanges(vkId)
    }

    override suspend fun updateFavFriends(vkId: Long, friendsIds: List<Long>) {
        clients.document("$vkId").update(FAV_FRIENDS, friendsIds)
        checkChanges(vkId)
    }

    private fun checkChanges(vkId: Long) {
        curClientChanged = vkId == VK.getUserId().value
        isChanged.postValue(curClientChanged)
    }

    companion object {
        const val CLIENTS = "clients"
        const val WISHLISTS = "wishlists"
        const val CART = "cart"
        const val CALENDAR = "calendar"
        const val FAV_FRIENDS = "favFriendsIds"
        const val EVENTS = "events"
        const val GIFTS_IDS = "giftsInfo"
        const val INFO = "info"
    }
}
