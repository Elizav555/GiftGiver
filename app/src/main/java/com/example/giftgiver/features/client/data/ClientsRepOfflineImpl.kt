package com.example.giftgiver.features.client.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.calendar.data.room.CalendarDao
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.data.room.CartDao
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.wishlist.data.room.WishlistDao

class ClientsRepOfflineImpl(
    private val clientDao: ClientDao,
    private val calendarDao: CalendarDao,
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val roomMapper: RoomMapper
) : ClientsRepOffline {
    override suspend fun addClient(client: Client) {
        saveCalendar(client)
        saveCart(client)
        saveWishlists(client)
        clientDao.save(roomMapper.mapClientToRoom(client))
    }

    private suspend fun saveWishlists(client: Client) {
        val wishlistsR = client.wishlists.map { roomMapper.mapWishlistToRoom(it, client.vkId) }
        wishlistsR.forEach { wishlistDao.save(it) }
    }

    private suspend fun saveCart(client: Client) {
        val cartR = roomMapper.mapCartToRoom(client.cart, client.vkId)
        cartDao.save(cartR)
    }

    private suspend fun saveCalendar(client: Client) {
        val calendarR = roomMapper.mapCalendarToRoom(client.calendar, client.vkId)
        calendarDao.save(calendarR)
    }

    override suspend fun deleteClient(client: Client) {
        clientDao.deleteClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun updateClient(client: Client) {
        saveCalendar(client)
        saveCart(client)
        saveWishlists(client)
        clientDao.updateClient(roomMapper.mapClientToRoom(client))
    }

    override suspend fun getClientByVkId(vkId: Long): Client? {
        val wishlists = clientDao.getClientWithWishlists(vkId)?.wishlists?.map {
            roomMapper.mapWishlistFromRoom(it)
        } ?: listOf()
        val cart = clientDao.getClientWithCart(vkId)?.cart?.let {
            roomMapper.mapCartFromRoom(it)
        } ?: Cart()
        val calendar = clientDao.getClientWithCalendar(vkId)?.calendar?.let {
            roomMapper.mapCalendarFromRoom(it)
        } ?: Calendar()
        return clientDao.getClientByVkId(vkId)
            ?.let { roomMapper.mapClientFromRoom(it, wishlists, cart, calendar) }
    }
}
