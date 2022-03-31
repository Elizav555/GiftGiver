package com.example.giftgiver.features.user.data.vk.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class UserInfoVk(
    val vkId: Long = 0,
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
) : Parcelable {
    fun parse(json: JSONObject) = UserInfoVk(
        vkId = json.optLong("id", 0),
        bdate = json.optString("bdate", ""),
        about = json.optString("about", ""),
        photoMax = json.optString("photo_max", ""),
    )
}
