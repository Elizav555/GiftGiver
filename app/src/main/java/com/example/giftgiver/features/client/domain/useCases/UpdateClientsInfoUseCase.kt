package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.client.domain.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateClientsInfoUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, info: UserInfo) =
        withContext(dispatcher) {
            clientsRepository.updateInfo(vkId, info)
        }
}
