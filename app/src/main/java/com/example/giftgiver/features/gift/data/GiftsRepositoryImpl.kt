package com.example.giftgiver.features.gift.data

import android.util.Log
import com.example.giftgiver.features.client.data.fb.CLIENTS
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.gift.data.fb.GiftFB
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftsRepository
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val GIFTS = "gifts"

class GiftsRepositoryImpl(
    private val fbMapper: FBMapper,
    private val firebaseFirestore: FirebaseFirestore,
) : GiftsRepository {
    private val clients = firebaseFirestore.collection(CLIENTS)

    override suspend fun addGift(clientId: Long, gift: Gift, wishlists: List<Wishlist>): String {
        val clientRef = clients.document(clientId.toString())
        return suspendCoroutine { continuation ->
            clientRef.collection(GIFTS).add(fbMapper.mapGiftToFB(gift))
                .addOnSuccessListener { documentReference ->
                    wishlists[gift.wishlistIndex].giftsIds.add(documentReference.id)
                    continuation.resume(documentReference.id)
                }
                .addOnFailureListener { e ->
                    Log.w("firebase", "Error adding document", e)
                    continuation.resumeWithException(e)
                }
        }
    }

    override suspend fun deleteGift(clientId: Long, gift: Gift, wishlists: List<Wishlist>) {
        val clientRef = clients.document(clientId.toString())
        clientRef.collection(GIFTS).document(gift.id).delete()
            .addOnSuccessListener {
                wishlists[gift.wishlistIndex].giftsIds.remove(gift.id)
            }
            .addOnFailureListener { e ->
                Log.w("firebase", "Error adding document", e)
            }
    }

    override suspend fun updateGift(clientId: Long, gift: Gift) {
        val clientRef = clients.document(clientId.toString())
        clientRef.collection(GIFTS).document(gift.id).set(fbMapper.mapGiftToFB(gift))
    }

    override suspend fun getGift(clientId: Long, giftId: String): Gift? {
        val clientRef = clients.document(clientId.toString())
        val snapshot = getDataSnapshot(clientRef, giftId)
        val giftFB = snapshot.toObject<GiftFB>()
        return giftFB?.let { fbMapper.mapGiftFromFB(it, giftId) }
    }

    private suspend fun getDataSnapshot(
        clientRef: DocumentReference,
        giftId: String
    ): DocumentSnapshot {
        return suspendCoroutine { continuation ->
            clientRef.collection(GIFTS).document(giftId).get().addOnSuccessListener {
                continuation.resume(it)
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        }
    }
}
