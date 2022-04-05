package com.example.giftgiver.features.user.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import javax.inject.Inject

class LoadFriendsUseCase @Inject constructor(
    private val loadFriendsVK: LoadFriendsVK,
    private val clientsRepOffline: ClientsRepOffline,
) {
    suspend operator fun invoke(vkId: Long) = run {
        try {
            loadFriendsVK.loadFriends(vkId)
        } catch (ex: Exception) {
            clientsRepOffline.getClientByVkId(vkId)?.favFriendsIds?.mapNotNull {
                clientsRepOffline.getClientByVkId(it)?.info
            } ?: listOf()
        }
    }
}