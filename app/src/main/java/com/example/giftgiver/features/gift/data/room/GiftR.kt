package com.example.giftgiver.features.gift.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "gifts")
@Parcelize
data class GiftR(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean = false,
    val wishlistIndex: Int = 0
) : Parcelable
