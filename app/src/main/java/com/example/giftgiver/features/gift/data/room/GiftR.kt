package com.example.giftgiver.features.gift.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "gifts")
@Parcelize
data class GiftR(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val forId: Long,
    val forName: String?,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean,
    var lastChanged: Date?,
    val wishlistIndex: Int
) : Parcelable
