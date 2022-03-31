package com.example.giftgiver.features.wishlist.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.gift.data.room.GiftR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wishlists")
@Parcelize
data class WishlistR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val gifts: List<GiftR> = listOf(),
) : Parcelable
