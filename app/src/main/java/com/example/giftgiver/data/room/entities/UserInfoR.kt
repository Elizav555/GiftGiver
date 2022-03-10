package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_info")
@Parcelize
data class UserInfoR(
    @PrimaryKey(autoGenerate = false) val vkId: Long = 0,
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
    val wishlists: List<WishlistR> = listOf(),
) : Parcelable
