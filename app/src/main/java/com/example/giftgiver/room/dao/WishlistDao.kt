package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.WishlistR

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(wishlist: WishlistR)

    @Update
    suspend fun updateWishlists(wishlist: WishlistR)

    @Query("SELECT * FROM  wishlists")
    suspend fun getWishlists(): MutableList<WishlistR>

    @Query("SELECT * FROM wishlists WHERE id = :id")
    suspend fun getWishlistById(id: Int): WishlistR?

    @Delete
    suspend fun deleteWishlist(wishlist: WishlistR)

    @Query("DELETE FROM wishlists")
    suspend fun deleteAllWishlists()
}
