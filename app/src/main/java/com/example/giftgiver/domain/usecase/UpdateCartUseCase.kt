package com.example.giftgiver.domain.usecase

import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.repositories.ClientsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(vkId: Long, gifts: List<Gift>) =
        withContext(dispatcher) {
            clientsRepository.updateCart(vkId, gifts)
        }
}
