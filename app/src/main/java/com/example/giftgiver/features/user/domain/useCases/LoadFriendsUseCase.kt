package com.example.giftgiver.features.user.domain.useCases

import android.util.Log
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import javax.inject.Inject

class LoadFriendsUseCase @Inject constructor(
    private val loadFriendsVK: LoadFriendsVK,
    private val clientsRepOffline: ClientsRepOffline,
) {
    suspend operator fun invoke(vkId: Long, filter: Boolean = true) = run {
        try {
            loadFriendsVK.loadFriends(vkId, filter)
        } catch (ex: Exception) {
            Log.e("load friends", ex.toString())
            clientsRepOffline.getClientByVkId(vkId)?.favFriendsIds?.mapNotNull {
                clientsRepOffline.getClientByVkId(it)?.info
            } ?: listOf()
        }
    }
}
