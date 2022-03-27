package com.example.giftgiver.data.vk.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class UserVk(
    val vkId: Long = 0,
    val name: String = "",
    val photo: String = "",
    var infoVk: UserInfoVk? = null,
    var bdate: String? = null,
) : Parcelable {
    fun parse(json: JSONObject) = UserVk(
        vkId = json.optLong("id", 0),
        name = json.optString("first_name", "") + " " + json.optString("last_name", ""),
        photo = json.optString("photo_100", ""),
        bdate = json.optString("bdate")
    )
}
