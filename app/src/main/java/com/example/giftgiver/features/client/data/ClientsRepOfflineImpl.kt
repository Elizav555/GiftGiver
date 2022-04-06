package com.example.giftgiver.features.client.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline

class ClientsRepOfflineImpl(
    private val clientDao: ClientDao,
    private val roomMapper: RoomMapper
) : ClientsRepOffline {
    private var hasInternet = true
    private val hasInternetConnection: MutableLiveData<Boolean> = MutableLiveData(hasInternet)

    init {
        hasInternetConnection.postValue(hasInternet)
    }

    override fun hasInternetConnection(): LiveData<Boolean> = hasInternetConnection

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
        hasInternet = false
        hasInternetConnection.postValue(hasInternet)
        return clientDao.getClientByVkId(vkId)
            ?.let { roomMapper.mapClientFromRoom(it) }
    }
}
