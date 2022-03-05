package com.example.giftgiver.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class UserInfo(
    val id: Int = 0,
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
    val wishlists: List<Wishlist> = listOf(),
) : Parcelable {
    fun parse(json: JSONObject) = UserInfo(
        id = json.optInt("id", 0),
        bdate = json.optString("bdate", ""),
        about = json.optString("about", ""),
        photoMax = json.optString("photo_max", ""),
    )
}
