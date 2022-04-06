package com.example.giftgiver.features.client.domain.useCases

import androidx.lifecycle.LiveData
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.start.domain.AuthRepository
import javax.inject.Inject

class ClientsChangesUseCase @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsRepOffline: ClientsRepOffline,
    private val authRepository: AuthRepository
) {
    fun isClientChanged(): LiveData<Boolean> = clientsRepository.isClientChanged()

    fun isClientAuth(): LiveData<Boolean?> = authRepository.isAuth()

    fun hasInternetConnection(): LiveData<Boolean> = clientsRepOffline.hasInternetConnection()
}
