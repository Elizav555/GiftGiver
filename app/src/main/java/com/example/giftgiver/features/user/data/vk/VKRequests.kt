package com.example.giftgiver.features.user.data.vk

import com.example.giftgiver.features.user.data.vk.entities.UserInfoVk
import com.example.giftgiver.features.user.data.vk.entities.UserVk
import com.example.giftgiver.features.user.domain.UserInfo
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VKFriendsRequest(id: Long?, private val vkMapper: VkMapper) :
    VKRequest<List<UserInfo>>("friends.get") {
    init {
        if (id != null) {
            addParam("user_id", id)
        }
        addParam("lang", 0)
        addParam("fields", "bdate,photo_100")
    }

    override fun parse(r: JSONObject): List<UserInfo> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<UserInfo>()
        for (i in 0 until users.length()) {
            result.add(vkMapper.mapUser(UserVk().parse(users.getJSONObject(i))))
        }
        return result
    }
}

class VKUserWithInfoRequest(id: Long?, private val vkMapper: VkMapper) :
    VKRequest<UserInfo>("users.get") {
    init {
        addParam("access_token", VKAccessToken.toString())
        if (id != null) {
            addParam("user_ids", id)
        }
        addParam("fields", "bdate, about, photo_max, photo_100")
    }

    override fun parse(r: JSONObject): UserInfo {
        val userInfoVk = r.getJSONArray("response")
        val userInfo = UserInfoVk().parse(userInfoVk.getJSONObject(0))
        val user = UserVk().parse(userInfoVk.getJSONObject(0))
        return vkMapper.mapUserWithInfo(user, userInfo)
    }
}
