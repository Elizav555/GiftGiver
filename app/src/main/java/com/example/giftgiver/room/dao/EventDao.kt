package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.EventR

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(event: EventR)

    @Update
    fun updateEvents(event: EventR)

    @Query("SELECT * FROM events")
    fun getEvents(): MutableList<EventR>

    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): EventR?

    @Delete
    fun deleteEvent(event: EventR)

    @Query("DELETE FROM events")
    fun deleteAllEvents()
}
