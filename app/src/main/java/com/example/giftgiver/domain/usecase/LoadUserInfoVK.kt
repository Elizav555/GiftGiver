package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.domain.entities.UserInfo
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoadUserInfoVK {
    fun loadInfo(vkId: Long, successAction: (UserInfo) -> Unit) {
        VK.execute(
            VKUserWithInfoRequest(vkId),
            object : VKApiCallback<UserInfo> {
                override fun success(result: UserInfo) {
                    successAction(result)
                }

                override fun fail(error: Exception) {
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }

    suspend fun loadInfo(vkId: Long): UserInfo {
        return suspendCoroutine { continuation ->
            VK.execute(
                VKUserWithInfoRequest(vkId),
                object : VKApiCallback<UserInfo> {
                    override fun success(result: UserInfo) {
                        continuation.resume(result)
                    }

                    override fun fail(error: Exception) {
                        continuation.resumeWithException(error)
                    }
                }
            )
        }
    }
}
