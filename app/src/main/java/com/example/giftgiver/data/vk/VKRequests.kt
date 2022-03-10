package com.example.giftgiver.data.vk

import com.example.giftgiver.data.mappers.VkMapper
import com.example.giftgiver.data.vk.entities.UserInfoVk
import com.example.giftgiver.data.vk.entities.UserVk
import com.example.giftgiver.domain.entities.User
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKFriendsRequest(id: Long?) : VKRequest<List<User>>("friends.get") {
    init {
        if (id != null) {
            addParam("user_id", id)
        }
        addParam("lang", 0)
        addParam("fields", "photo_100")
    }

    override fun parse(r: JSONObject): List<User> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<User>()
        for (i in 0 until users.length()) {
            result.add(VkMapper().mapUser(UserVk().parse(users.getJSONObject(i))))
        }
        return result
    }
}

class VKUserWithInfoRequest(id: Long?) : VKRequest<User>("users.get") {
    init {
        addParam("access_token", VKAccessToken.toString())
        if (id != null) {
            addParam("user_ids", id)
        }
        addParam("fields", "bdate, about, photo_max, photo_100")
    }

    override fun parse(r: JSONObject): User {
        val userInfoVk = r.getJSONArray("response")
        val userInfo = UserInfoVk().parse(userInfoVk.getJSONObject(0))
        val user = UserVk().parse(userInfoVk.getJSONObject(0))
        return VkMapper().mapUserWithInfo(user, userInfo)
    }
}
