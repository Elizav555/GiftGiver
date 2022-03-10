package com.example.giftgiver.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "carts")
@Parcelize
data class CartR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val gifts: List<GiftR> = listOf(),
) : Parcelable
