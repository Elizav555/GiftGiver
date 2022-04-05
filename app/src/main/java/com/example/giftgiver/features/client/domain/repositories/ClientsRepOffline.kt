package com.example.giftgiver.features.client.domain.repositories

import com.example.giftgiver.features.client.domain.Client

interface ClientsRepOffline {
    suspend fun addClient(client: Client)

    suspend fun deleteClient(client: Client)

    suspend fun updateClient(client: Client)

    suspend fun getClientByVkId(vkId: Long): Client?
}
