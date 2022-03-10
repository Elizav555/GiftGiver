package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.CartR

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(cart: CartR)

    @Update
    suspend fun updateCarts(cart: CartR)

    @Query("SELECT * FROM carts")
    suspend fun getCarts(): MutableList<CartR>

    @Query("SELECT * FROM carts WHERE id = :id")
    suspend fun getCartById(id: Int): CartR?

    @Delete
    suspend fun deleteCart(cart: CartR)

    @Query("DELETE FROM carts")
    suspend fun deleteAllCarts()
}
