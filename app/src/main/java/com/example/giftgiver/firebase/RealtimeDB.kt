package com.example.giftgiver.firebase

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

const val FIREBASE_URL = "https://giftgiver-938fc-default-rtdb.europe-west1.firebasedatabase.app"
const val CLIENTS = "clients"

class RealtimeDB {
    private val database = Firebase.database(FIREBASE_URL).reference

    fun addNewClient(vkId: Long, addInfo: String, wishlists: List<String>) {
        val client = Client(vkId, addInfo, wishlists)

        database.child(CLIENTS).setValue(client).addOnCompleteListener {
            Log.println(
                Log.VERBOSE,
                "clientsFB",
                "Success"
            )
        }.addOnFailureListener {
            Log.println(
                Log.VERBOSE,
                "clientsFB",
                "Fail"
            )
        }.addOnCanceledListener {
            Log.println(
                Log.VERBOSE,
                "clientsFB",
                "Cancel"
            )
        }
    }
}
