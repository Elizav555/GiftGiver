package com.example.giftgiver.firebase

import android.util.Log
import com.example.giftgiver.entities.*
import com.example.giftgiver.entities.Calendar
import com.google.android.gms.tasks.Task
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

const val FIREBASE_URL = "https://giftgiver-938fc-default-rtdb.europe-west1.firebasedatabase.app"
const val CLIENTS = "clients"
const val CALENDARS = "calendars"
const val CARTS = "carts"
const val EVENTS = "events"
const val GIFTS = "gifts"
const val WISHLISTS = "wishlists"

class RealtimeDB {
    private val database = Firebase.database(FIREBASE_URL).reference

    fun addNewClient(
        vkId: Long,
        calendar: Calendar,
        user: User,
        cart: Cart,
        favFriends: List<User>
    ) {
        val client = Client(vkId, calendar, user, cart, favFriends)
        database.child(CLIENTS).child(client.vkId.toString()).setValue(client).setListeners(CLIENTS)
    }

    fun addNewCalendar(id: Long, events: List<Event>) {
        val calendar = Calendar(id, events)
        database.child(CALENDARS).child(calendar.id.toString()).setValue(calendar)
            .setListeners(CALENDARS)
    }

    fun addNewCart(id: Long, gifts: List<Gift>) {
        val cart = Cart(id, gifts)
        database.child(CARTS).child(cart.id.toString()).setValue(cart)
            .setListeners(CARTS)
    }

    fun addNewEvent(id: Long, name: String, date: Date, desc: String?) {
        val event = Event(id, name, date, desc)
        database.child(EVENTS).child(event.id.toString()).setValue(event)
            .setListeners(EVENTS)
    }

    fun addNewGift(id: Long, name: String, desc: String?, imageUrl: String?, isChosen: Boolean) {
        val gift = Gift(id, name, desc, imageUrl, isChosen)
        database.child(GIFTS).child(gift.id.toString()).setValue(gift).setListeners(GIFTS)
    }

    fun addNewWishlist(id: Long, name: String, gifts: List<Gift>) {
        val wishlist = Wishlist(id, name, gifts)
        database.child(WISHLISTS).child(wishlist.id.toString()).setValue(wishlist)
            .setListeners(WISHLISTS)
    }

    private fun Task<Void>.setListeners(tag: String) {
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
