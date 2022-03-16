package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.domain.entities.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    suspend fun loadInfo(vkId: Long): User {
        return suspendCoroutine { continuation ->
            VK.execute(
                VKUserWithInfoRequest(vkId),
                object : VKApiCallback<User> {
                    override fun success(result: User) {
                        continuation.resume(result)
                    }

                    override fun fail(error: Exception) {
                        continuation.resumeWithException(error)
                    }
                }
            )
        }
    }

//    suspend fun loadInfo(vkId: Long) = withContext(Dispatchers.Default) {
//        VK.executeSync(
//            VKUserWithInfoRequest(vkId)
//        )
//    }
}
