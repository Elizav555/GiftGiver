package com.example.giftgiver.domain.usecase

import android.util.Log
import com.example.giftgiver.data.vk.VKFriendsRequest
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoadFriendsVK(
    private val clientsRep: ClientsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) {
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

    suspend fun loadFriends(vkId: Long): List<User> {
        val friendsVK = loadAllFriends(vkId)
        return friendsVK.filter { friend -> clientsRep.getClientByVkId(friend.vkId).data != null }
    }

    private suspend fun loadAllFriends(vkId: Long): List<User> {
        return suspendCoroutine { continuation ->
            VK.execute(
                VKFriendsRequest(vkId),
                object : VKApiCallback<List<User>> {
                    override fun success(result: List<User>) {
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
