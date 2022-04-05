package com.example.giftgiver.features.client.domain.useCases

import androidx.lifecycle.LiveData
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import javax.inject.Inject

class ClientUpdatedUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository
) {
    fun isClientChanged(): LiveData<Boolean> = clientsRepository.isClientChanged()
}