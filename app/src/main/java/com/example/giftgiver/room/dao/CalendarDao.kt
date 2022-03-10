package com.example.giftgiver.room.dao

import androidx.room.*
import com.example.giftgiver.room.entities.CalendarR

@Dao
interface CalendarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(calendar: CalendarR)

    @Update
    fun updateCalendars(calendar: CalendarR)

    @Query("SELECT * FROM calendars")
    fun getCalendars(): MutableList<CalendarR>

    @Query("SELECT * FROM calendars WHERE id = :id")
    fun getCalendarById(id: Int): CalendarR?

    @Delete
    fun deleteCalendar(calendar: CalendarR)

    @Query("DELETE FROM calendars")
    fun deleteAllCalendars()
}
