package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users_info")
@Parcelize
data class UserInfoR(
    @PrimaryKey(autoGenerate = false) val vkId: Long = 0,
    val name: String = "",
    val photo: String = "",
    val bdate: String? = "",
    val about: String? = "",
    val photoMax: String? = "",
) : Parcelable
