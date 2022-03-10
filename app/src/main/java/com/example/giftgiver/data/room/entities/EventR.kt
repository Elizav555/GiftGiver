package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "events")
@Parcelize
data class EventR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val date: Date,
    val desc: String?,
) : Parcelable
