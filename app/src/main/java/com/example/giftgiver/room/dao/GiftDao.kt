package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.GiftR

@Dao
interface GiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(gift: GiftR)

    @Update
    fun updateGifts(gift: GiftR)

    @Query("SELECT * FROM gifts")
    fun getGifts(): MutableList<GiftR>

    @Query("SELECT * FROM gifts WHERE id = :id")
    fun getGiftById(id: Int): GiftR?

    @Delete
    fun deleteGift(gift: GiftR)

    @Query("DELETE FROM gifts")
    fun deleteAllGifts()
}
