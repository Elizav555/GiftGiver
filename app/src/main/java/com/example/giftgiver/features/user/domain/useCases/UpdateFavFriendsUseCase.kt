package com.example.giftgiver.features.user.domain.useCases

import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFavFriendsUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, friendsIds: List<Long>) =
        withContext(dispatcher) {
            clientsRepository.updateFavFriends(vkId, friendsIds)
        }
}
