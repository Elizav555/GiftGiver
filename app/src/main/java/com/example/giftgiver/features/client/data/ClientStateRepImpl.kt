package com.example.giftgiver.features.client.data

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep

class ClientStateRepImpl : ClientStateRep {
    private var curClient: Client? = null
    override fun addClient(client: Client) {
        curClient = client
    }

    override fun deleteClient() {
        curClient = null
    }

    override fun getClient(): Client? = curClient
}