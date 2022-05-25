package com.example.giftgiver.features.client.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ClientsRepOfflineImpl(
    private val clientDao: ClientDao,
    private val roomMapper: RoomMapper
) : ClientsRepOffline {
    private var hasInternet = true
    private val hasInternetConnection =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        hasInternetConnection.tryEmit(hasInternet)
    }

    override fun hasInternetConnection(): SharedFlow<Boolean> = hasInternetConnection

    override suspend fun addClient(newClient: Client): Unit = coroutineScope {
        with(roomMapper.mapClientToRoom(newClient)) {
            val clientAdded = async { clientDao.save(client) }
            val eventsAdded = async { events.forEach { clientDao.save(it) } }
            val wishlistsAdded = async { wishlists.forEach { clientDao.save(it) } }
            val giftsAdded = async { giftsInfo.forEach { clientDao.save(it) } }
            awaitAll(clientAdded, eventsAdded, wishlistsAdded, giftsAdded)
        }
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(roomMapper.mapClientToRoom(client).client)
    }

    override suspend fun updateClient(newClient: Client): Unit = coroutineScope {
        with(roomMapper.mapClientToRoom(newClient)) {
            val clientAdded = async { clientDao.updateClient(client) }
            val eventsAdded = async { events.forEach { clientDao.updateEvent(it) } }
            val wishlistsAdded = async { wishlists.forEach { clientDao.updateWishlist(it) } }
            val giftsAdded = async { giftsInfo.forEach { clientDao.updateGiftInfo(it) } }
            awaitAll(clientAdded, eventsAdded, wishlistsAdded, giftsAdded)
        }
    }

    override suspend fun getClientByVkId(vkId: Long): Client? {
        if (hasInternet) {
            hasInternet = false
            hasInternetConnection.tryEmit(hasInternet)
        }
        return clientDao.getClientByVkId(vkId)
            ?.let { roomMapper.mapClientFromRoom(it) }
    }
}
