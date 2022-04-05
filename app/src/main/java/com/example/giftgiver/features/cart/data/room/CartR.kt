package com.example.giftgiver.features.cart.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "carts")
@Parcelize
data class CartR(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long,
    val giftsInfo: List<GiftInfoR>
) : Parcelable

