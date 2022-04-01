package com.example.giftgiver.features.user.domain.useCases

import android.util.Log
import com.example.giftgiver.features.client.domain.ClientsRepository
import com.example.giftgiver.features.user.data.vk.VKFriendsRequest
import com.example.giftgiver.features.user.domain.UserInfo
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoadFriendsVK @Inject constructor(
    private val clientsRep: ClientsRepository
) {
    fun loadFriends(vkId: Long, successAction: (List<UserInfo>) -> Unit) {
        VK.execute(
            VKFriendsRequest(vkId),
            object : VKApiCallback<List<UserInfo>> {
                override fun success(result: List<UserInfo>) {
                    successAction(result)
                }

                override fun fail(error: Exception) {
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }

    suspend fun loadFriends(vkId: Long): List<UserInfo> {
        val friendsVK = loadAllFriends(vkId)
        return friendsVK.sortedBy { user -> user.name }//todo change back
//        return friendsVK.mapNotNull { friend -> clientsRep.getClientByVkId(friend.vkId)?.info }
//            .sortedByDescending { user -> user.name }
    }

    private suspend fun loadAllFriends(vkId: Long): List<UserInfo> {
        return suspendCoroutine { continuation ->
            VK.execute(
                VKFriendsRequest(vkId),
                object : VKApiCallback<List<UserInfo>> {
                    override fun success(result: List<UserInfo>) {
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
