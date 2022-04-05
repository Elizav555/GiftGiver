package com.example.giftgiver.features.gift.data

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.repositories.GiftsRepOffline

class GiftsRepOfflineImpl(private val giftDao: GiftDao, private val roomMapper: RoomMapper) :
    GiftsRepOffline {
    override suspend fun addGift(gift: Gift) {
        giftDao.save(roomMapper.mapGiftToRoom(gift))
    }

    override suspend fun deleteGift(gift: Gift) {
        giftDao.deleteGift(roomMapper.mapGiftToRoom(gift))
    }

    override suspend fun getGift(giftId: String): Gift? {
        return giftDao.getGiftById(giftId)?.let { roomMapper.mapGiftFromRoom(it) }
    }
}
