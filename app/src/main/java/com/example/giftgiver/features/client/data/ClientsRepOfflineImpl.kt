package com.example.giftgiver.features.client.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline

class ClientsRepOfflineImpl(
    private val clientDao: ClientDao,
    private val roomMapper: RoomMapper
) : ClientsRepOffline {
    override suspend fun addClient(client: Client) {
        val clientR = roomMapper.mapClientToRoom(client)
        clientDao.save(clientR)
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun updateClient(client: Client) {
        clientDao.updateClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun getClientByVkId(vkId: Long): Client? {
        return clientDao.getClientByVkId(vkId)
            ?.let { roomMapper.mapClientFromRoom(it) }
    }
}
