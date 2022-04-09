package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import javax.inject.Inject

class GetAllClientsUseCase @Inject constructor(private val clientsRep: ClientsRepository) {
    suspend operator fun invoke() = clientsRep.getAllClientsIds()
}