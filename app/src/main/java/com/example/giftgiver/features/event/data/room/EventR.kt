package com.example.giftgiver.features.event.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "events")
@Parcelize
data class EventR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val date: Calendar,
    val desc: String?,
) : Parcelable
