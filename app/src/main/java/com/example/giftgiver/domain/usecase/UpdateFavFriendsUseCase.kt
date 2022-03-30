package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.repositories.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFavFriendsUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, friends: List<Client>) =
        withContext(dispatcher) {
            clientsRepository.updateFavFriends(vkId, friends)
        }
}
