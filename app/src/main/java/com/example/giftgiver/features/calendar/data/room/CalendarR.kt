package com.example.giftgiver.features.calendar.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.event.data.room.EventR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "calendars")
@Parcelize
data class CalendarR(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long,
    val events: List<EventR>
) : Parcelable
