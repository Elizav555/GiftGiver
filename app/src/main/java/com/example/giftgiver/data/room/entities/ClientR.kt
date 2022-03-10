package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "clients")
@Parcelize
data class ClientR(
    @PrimaryKey(autoGenerate = false) val vkId: Long? = 0,
    val calendar: CalendarR,
    val user: UserR,
    val cart: CartR,
    val favFriends: List<UserR> = listOf(),
) : Parcelable
