package com.example.giftgiver.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class UserR(
    @PrimaryKey(autoGenerate = false) val vkId: Long = 0,
    val name: String = "",
    val photo: String = "",
    var info: UserInfoR? = null
) : Parcelable
