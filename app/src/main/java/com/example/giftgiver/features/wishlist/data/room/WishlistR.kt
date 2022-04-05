package com.example.giftgiver.features.wishlist.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wishlists")
@Parcelize
data class WishlistR(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long,
    val name: String,
    var giftsIds: List<String> = listOf(),
) : Parcelable
