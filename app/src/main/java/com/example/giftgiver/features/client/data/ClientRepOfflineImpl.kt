package com.example.giftgiver.features.client.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.calendar.data.room.CalendarDao
import com.example.giftgiver.features.cart.data.room.CartDao
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientRepOffline
import com.example.giftgiver.features.event.data.room.EventDao
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.wishlist.data.room.WishlistDao

class ClientRepOfflineImpl(
    private val clientDao: ClientDao,
    private val calendarDao: CalendarDao,
    private val cartDao: CartDao,
    private val eventDao: EventDao,
    private val giftDao: GiftDao,
    private val wishlistDao: WishlistDao,
    private val roomMapper: RoomMapper
) : ClientRepOffline {
    override suspend fun addClient(client: Client) {
        client.calendar.events.forEach { eventDao.save(roomMapper.mapEventToRoom(it)) }
        clientDao.save(roomMapper.mapClientToRoom(client))
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun updateClient(client: Client) {
        clientDao.updateClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun getClientByVkId(vkId: Long) =
        clientDao.getClientByVkId(vkId)?.let { roomMapper.mapClientFromRoom(it) }
}
