package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.EventR

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(event: EventR)

    @Update
    suspend fun updateEvents(event: EventR)

    @Query("SELECT * FROM events")
    suspend fun getEvents(): MutableList<EventR>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Int): EventR?

    @Delete
    suspend fun deleteEvent(event: EventR)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
}
