package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.start.domain.AuthRepository
import javax.inject.Inject

class ClientsChangesInteractor @Inject constructor(
    private val clientsRepository: ClientsRepository,
    private val clientsRepOffline: ClientsRepOffline,
    private val authRepository: AuthRepository
) {
    fun isClientChanged() = clientsRepository.isClientChanged()

    fun isClientAuth() = authRepository.isAuth()

    fun hasInternetConnection() = clientsRepOffline.hasInternetConnection()
}
