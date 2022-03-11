package com.example.giftgiver.data.firebase

import android.util.Log
import com.example.giftgiver.data.firebase.entities.*
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.reflect.full.declaredMemberProperties

const val CLIENTS = "clients"
const val CALENDARS = "calendars"
const val CARTS = "carts"
const val EVENTS = "events"
const val GIFTS = "gifts"
const val WISHLISTS = "wishlists"
const val USERS = "users"

class FirestoreDB {
    private val database = FirebaseFirestore.getInstance()
    val clients = database.collection(CLIENTS)
    val calendars = database.collection(CALENDARS)
    val carts = database.collection(CARTS)
    val events = database.collection(EVENTS)
    val gifts = database.collection(GIFTS)
    val wishlists = database.collection(WISHLISTS)
    val users = database.collection(USERS)

    fun addNewClient(client: ClientFB) {
        val map = ClientFB::class.declaredMemberProperties.associate { it.name to it.get(client) }
        clients.add(map).setListeners(CLIENTS)
    }

    fun addNewCalendar(calendar: CalendarFB) {
        val map =
            CalendarFB::class.declaredMemberProperties.associate { it.name to it.get(calendar) }
        calendars.add(map).setListeners(CALENDARS)
    }

    fun addNewCart(cart: CartFB) {
        val map = CartFB::class.declaredMemberProperties.associate { it.name to it.get(cart) }
        carts.add(map).setListeners(CARTS)
    }

    fun addNewEvent(event: EventFB) {
        val map = EventFB::class.declaredMemberProperties.associate { it.name to it.get(event) }
        events.add(map).setListeners(EVENTS)
    }

    fun addNewGift(gift: GiftFB) {
        val map = GiftFB::class.declaredMemberProperties.associate { it.name to it.get(gift) }
        gifts.add(map).setListeners(GIFTS)
    }

    fun addNewWishlist(wishlist: WishlistFB) {
        val map =
            WishlistFB::class.declaredMemberProperties.associate { it.name to it.get(wishlist) }
        wishlists.add(map).setListeners(WISHLISTS)
    }

    fun addNewUser(user: UserFB) {
        val map = UserFB::class.declaredMemberProperties.associate { it.name to it.get(user) }
        users.add(map).setListeners(USERS)
    }

    private fun Task<DocumentReference>.setListeners(tag: String) {
        this.addOnCompleteListener {
            Log.println(
                Log.VERBOSE,
                tag,
                "Success"
            )
        }.addOnFailureListener {
            Log.println(
                Log.VERBOSE,
                tag,
                "Fail"
            )
        }.addOnCanceledListener {
            Log.println(
                Log.VERBOSE,
                tag,
                "Cancel"
            )
        }
    }
}