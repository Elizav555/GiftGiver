package com.example.giftgiver.features.cart.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.gift.data.room.GiftR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "carts")
@Parcelize
data class CartR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val gifts: List<GiftR> = listOf(),
) : Parcelable
