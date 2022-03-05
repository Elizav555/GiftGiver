package com.example.giftgiver.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class User(
    val id: Int = 0,
    val name: String = "",
    val photo: String = "",
    var info: UserInfo? = null
) : Parcelable {
    fun parse(json: JSONObject) = User(
        id = json.optInt("id", 0),
        name = json.optString("first_name", "") + " " + json.optString("last_name", ""),
        photo = json.optString("photo_100", ""),
    )
}
