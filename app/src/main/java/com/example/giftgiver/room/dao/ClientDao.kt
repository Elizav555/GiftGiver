package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.ClientR

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(client: ClientR)

    @Update
    suspend fun updateClients(client: ClientR)

    @Query("SELECT * FROM clients")
    suspend fun getClients(): MutableList<ClientR>

    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    suspend fun getClientByVkId(vkId: Int): ClientR?

    @Delete
    suspend fun deleteClient(client: ClientR)

    @Query("DELETE FROM clients")
    suspend fun deleteAllClients()
}
