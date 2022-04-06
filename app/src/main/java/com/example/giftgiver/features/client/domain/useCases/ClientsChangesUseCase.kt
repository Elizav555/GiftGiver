package com.example.giftgiver.features.client.domain.useCases

import androidx.lifecycle.LiveData
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import javax.inject.Inject

class ClientsChangesUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsRepOffline: ClientsRepOffline
) {
    fun isClientChanged(): LiveData<Boolean> = clientsRepository.isClientChanged()

    fun hasInternetConnection(): LiveData<Boolean> = clientsRepOffline.hasInternetConnection()
}
