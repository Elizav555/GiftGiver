package com.example.giftgiver.features.client.domain.repositories

import com.example.giftgiver.features.client.domain.Client
import kotlinx.coroutines.flow.SharedFlow

interface ClientsRepOffline {
    suspend fun addClient(newClient: Client)

    suspend fun deleteClient(client: Client)

    suspend fun updateClient(newClient: Client)

    suspend fun getClientByVkId(vkId: Long): Client?

    fun hasInternetConnection(): SharedFlow<Boolean>
}
