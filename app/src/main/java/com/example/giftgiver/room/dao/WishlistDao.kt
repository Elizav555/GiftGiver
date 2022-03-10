package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.WishlistR

@Dao
interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(wishlist: WishlistR)

    @Update
    fun updateWishlists(wishlist: WishlistR)

    @Query("SELECT * FROM  wishlists")
    fun getWishlists(): MutableList<WishlistR>

    @Query("SELECT * FROM wishlists WHERE id = :id")
    fun getWishlistById(id: Int): WishlistR?

    @Delete
    fun deleteWishlist(wishlist: WishlistR)

    @Query("DELETE FROM wishlists")
    fun deleteAllWishlists()
}
