package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.ClientR

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(client: ClientR)

    @Update
    fun updateClients(client: ClientR)

    @Query("SELECT * FROM clients")
    fun getClients(): MutableList<ClientR>

    @Query("SELECT * FROM clients WHERE vkId = :vkId")
    fun getClientByVkId(vkId: Int): ClientR?

    @Delete
    fun deleteClient(client: ClientR)

    @Query("DELETE FROM clients")
    fun deleteAllClients()
}
