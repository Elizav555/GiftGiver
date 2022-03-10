package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wishlists")
@Parcelize
data class WishlistR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val gifts: List<GiftR> = listOf(),
) : Parcelable
