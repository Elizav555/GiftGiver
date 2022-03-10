package com.example.giftgiver.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "calendars")
@Parcelize
data class CalendarR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val events: List<EventR> = listOf(),
) : Parcelable
