package com.example.giftgiver.features.gift.data.room

import androidx.room.*

@Dao
interface GiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(gift: GiftR)

    @Update
    suspend fun updateGifts(gift: GiftR)

    @Query("SELECT * FROM gifts")
    suspend fun getGifts(): MutableList<GiftR>

    @Query("SELECT * FROM gifts WHERE id = :id")
    suspend fun getGiftById(id: Int): GiftR?

    @Delete
    suspend fun deleteGift(gift: GiftR)

    @Query("DELETE FROM gifts")
    suspend fun deleteAllGifts()
}
