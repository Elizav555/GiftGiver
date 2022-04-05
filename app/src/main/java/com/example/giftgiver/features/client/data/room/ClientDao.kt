package com.example.giftgiver.features.client.data.room

import androidx.room.*

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(client: ClientR)

    @Update
    suspend fun updateClient(client: ClientR)

    @Query("SELECT * FROM clients")
    suspend fun getClients(): MutableList<ClientR>

    @Transaction
    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientWithCart(vkId: Long): ClientAndCart?

    @Transaction
    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientWithWishlists(vkId: Long): ClientWithWishlists?

    @Transaction
    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientWithCalendar(vkId: Long): ClientAndCalendar?

    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientByVkId(vkId: Long): ClientR?

    @Delete
    suspend fun deleteClient(client: ClientR)

    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()
}
