package com.example.giftgiver.features.client.domain.repositories

import com.example.giftgiver.features.client.domain.Client

interface ClientStateRep {
    fun addClient(client: Client)
    fun deleteClient()
    fun getClient(): Client?
}
