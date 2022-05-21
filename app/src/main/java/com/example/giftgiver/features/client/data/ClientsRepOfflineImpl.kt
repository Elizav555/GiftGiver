package com.example.giftgiver.features.client.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ClientsRepOfflineImpl(
    private val clientDao: ClientDao,
    private val roomMapper: RoomMapper,
    private val clientsRepository: ClientsRepository
) : ClientsRepOffline {
    private var hasInternet = true
    private val hasInternetConnection =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        hasInternetConnection.tryEmit(hasInternet)
    }

    override fun hasInternetConnection(): SharedFlow<Boolean> = hasInternetConnection

    override suspend fun addClient(newClient: Client) =
        with(roomMapper.mapClientToRoom(newClient)) {
            clientDao.save(client)
            events.forEach { clientDao.save(it) }
            wishlists.forEach { clientDao.save(it) }
            giftsInfo.forEach { clientDao.save(it) }

            addFavorites(client.favFriendsIds)
        }

    private suspend fun addFavorites(favFriendsIds: List<Long>) {
        val clients = clientDao.getClients()
        favFriendsIds.filter { id -> !clients.map { it.client.vkId }.contains(id) }
            .forEach { addClientByVkId(it) }
    }

    private suspend fun addClientByVkId(vkId: Long) {
        val client = clientsRepository.getClientByVkId(vkId)
        client?.let { addClient(it) }
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(roomMapper.mapClientToRoom(client).client)
    }

    override suspend fun updateClient(newClient: Client) =
        with(roomMapper.mapClientToRoom(newClient)) {
            clientDao.updateClient(client)
            events.forEach { clientDao.updateEvent(it) }
            wishlists.forEach { clientDao.updateWishlist(it) }
            giftsInfo.forEach { clientDao.updateGiftInfo(it) }
        }

    override suspend fun getClientByVkId(vkId: Long): Client? {
        hasInternet = false
        hasInternetConnection.tryEmit(hasInternet)
        return clientDao.getClientByVkId(vkId)
            ?.let { roomMapper.mapClientFromRoom(it) }
    }
}
