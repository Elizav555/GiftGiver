package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.CalendarR

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(calendar: CalendarR)

    @Update
    suspend fun updateCalendars(calendar: CalendarR)

    @Query("SELECT * FROM calendars")
    suspend fun getCalendars(): MutableList<CalendarR>

    @Query("SELECT * FROM calendars WHERE id = :id")
    suspend fun getCalendarById(id: Int): CalendarR?

    @Delete
    suspend fun deleteCalendar(calendar: CalendarR)

    @Query("DELETE FROM calendars")
    suspend fun deleteAllCalendars()
}
