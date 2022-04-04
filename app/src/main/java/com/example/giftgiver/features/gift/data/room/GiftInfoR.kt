package com.example.giftgiver.features.gift.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "gifts")
@Parcelize
data class GiftInfoR(
    @PrimaryKey(autoGenerate = true) val id: String,
    var giftId: String = "",
    var forId: Long = 0,
    var lastChanged: Date = Date(),
) : Parcelable