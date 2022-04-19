package com.example.giftgiver.features.client.data

import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ClientStateRepImpl : ClientStateRep {
    private val _clientFlow =
        MutableSharedFlow<Client?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var curClient: Client? = null

    init {
        _clientFlow.tryEmit(curClient)
    }

    override fun addClient(client: Client) {
        curClient = client
        _clientFlow.tryEmit(curClient)
    }

    override fun deleteClient() {
        curClient = null
        _clientFlow.tryEmit(curClient)
    }

    override fun getClient(): SharedFlow<Client?> = _clientFlow
}
