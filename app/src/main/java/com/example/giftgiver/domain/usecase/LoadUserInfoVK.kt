package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.domain.entities.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class LoadUserInfoVK {
    fun loadInfo(vkId: Long, successAction: (User) -> Unit) {
        VK.execute(
            VKUserWithInfoRequest(vkId),
            object : VKApiCallback<User> {
                override fun success(result: User) {
                    successAction(result)
                }

                override fun fail(error: Exception) {
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }
}
