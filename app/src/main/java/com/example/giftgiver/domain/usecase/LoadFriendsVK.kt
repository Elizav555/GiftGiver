package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKFriendsRequest
import com.example.giftgiver.domain.entities.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class LoadFriendsVK {
    fun loadFriends(vkId: Long, successAction: (List<User>) -> Unit) {
        VK.execute(
            VKFriendsRequest(vkId),
            object : VKApiCallback<List<User>> {
                override fun success(result: List<User>) {
                    successAction(result)
                }

                override fun fail(error: Exception) {
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }
}
