package com.example.giftgiver.features.client.domain.repositories

import com.example.giftgiver.features.client.domain.Client
import kotlinx.coroutines.flow.SharedFlow

interface ClientStateRep {
    fun addClient(client: Client)
    fun deleteClient()
    fun getClient(): SharedFlow<Client?>
}
