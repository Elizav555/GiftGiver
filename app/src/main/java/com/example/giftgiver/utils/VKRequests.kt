package com.example.giftgiver.utils

import com.example.giftgiver.entities.User
import com.example.giftgiver.entities.UserInfo
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKFriendsRequest(uid: Int = 0) : VKRequest<List<User>>("friends.get") {
    init {
        if (uid != 0) {
            addParam("user_id", uid)
        }
        addParam("lang", 0)
        addParam("fields", "photo_100")
    }

    override fun parse(r: JSONObject): List<User> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<User>()
        for (i in 0 until users.length()) {
            result.add(User().parse(users.getJSONObject(i)))
        }
        return result
    }
}

class VKUserInfoRequest(uid: Int = 0) : VKRequest<UserInfo>("users.get") {
    init {
        addParam("access_token", VKAccessToken.toString())
        if (uid != 0) {
            addParam("user_ids", uid)
        }
        addParam("fields", "bdate, about, photo_max")
    }

    override fun parse(r: JSONObject): UserInfo {
        val userInfo = r.getJSONArray("response")
        return UserInfo().parse(userInfo.getJSONObject(0))
    }
}
