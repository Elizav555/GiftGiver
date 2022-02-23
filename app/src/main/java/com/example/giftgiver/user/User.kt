package com.example.giftgiver.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val photo: String = "",
) : Parcelable {
    fun parse(json: JSONObject) = User(
        id = json.optInt("id", 0),
        firstName = json.optString("first_name", ""),
        lastName = json.optString("last_name", ""),
        photo = json.optString("photo_100", ""),
    )
}
