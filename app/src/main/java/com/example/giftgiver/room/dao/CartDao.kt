package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.CartR

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(cart: CartR)

    @Update
    fun updateCarts(cart: CartR)

    @Query("SELECT * FROM carts")
    fun getCarts(): MutableList<CartR>

    @Query("SELECT * FROM carts WHERE id = :id")
    fun getCartById(id: Int): CartR?

    @Delete
    fun deleteCart(cart: CartR)

    @Query("DELETE FROM carts")
    fun deleteAllCarts()
}
