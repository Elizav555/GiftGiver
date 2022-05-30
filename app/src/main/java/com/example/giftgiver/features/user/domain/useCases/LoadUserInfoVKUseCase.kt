package com.example.giftgiver.features.user.domain.useCases

import com.example.giftgiver.features.user.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.features.user.data.vk.VkMapper
import com.example.giftgiver.features.user.domain.UserInfo
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoadUserInfoVKUseCase @Inject constructor(private val vkMapper: VkMapper) {
//    fun loadInfo(vkId: Long, successAction: (UserInfo) -> Unit) {
//        VK.execute(
//            VKUserWithInfoRequest(vkId, vkMapper),
//            object : VKApiCallback<UserInfo> {
//                override fun success(result: UserInfo) {
//                    successAction(result)
//                }
//
//                override fun fail(error: Exception) {
//                    Log.println(Log.ERROR, "", error.toString())
//                }
//            }
//        )
//    }

    suspend operator fun invoke(vkId: Long): UserInfo {
        return suspendCoroutine { continuation ->
            VK.execute(
                VKUserWithInfoRequest(vkId, vkMapper),
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
