package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.UserInfo
import com.example.giftgiver.domain.repositories.ClientsRepository
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
