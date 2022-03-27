package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKFriendsRequest
import com.example.giftgiver.domain.entities.UserInfo
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoadFriendsVK(
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
//        return friendsVK.filter { friend -> clientsRep.getClientByVkId(friend.vkId) != null }
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
