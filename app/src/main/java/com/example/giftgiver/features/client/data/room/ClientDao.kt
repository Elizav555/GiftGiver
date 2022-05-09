package com.example.giftgiver.features.client.data.room

import androidx.room.*
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.wishlist.data.room.WishlistR

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(client: ClientR)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(event: EventR)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(giftInfo: GiftInfoR)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(wishlist: WishlistR)

    @Update
    suspend fun updateClient(client: ClientR)

    @Update
    suspend fun updateGiftInfo(giftInfo: GiftInfoR)

    @Update
    suspend fun updateWishlist(wishlist: WishlistR)

    @Update
    suspend fun updateEvent(event: EventR)

    @Transaction
    @Query("SELECT * FROM clients")
    suspend fun getClients(): MutableList<ClientFullR>

    @Transaction
    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientByVkId(vkId: Long): ClientFullR?

    @Delete
    suspend fun deleteClient(client: ClientR)

    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()
}
