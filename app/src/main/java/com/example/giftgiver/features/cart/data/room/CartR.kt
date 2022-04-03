package com.example.giftgiver.features.cart.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "carts")
@Parcelize
data class CartR(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var giftsIdsAndFor: List<Pair<String, Long>> = listOf(),
) : Parcelable
