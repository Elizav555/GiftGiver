package com.example.giftgiver.features.user.data.room

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
) : Parcelable