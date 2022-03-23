package com.example.giftgiver.data.room.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "gifts")
@Parcelize
data class GiftR(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val forId: Long = 0,
    val forName: String? = null,
    val desc: String?,
    val imageUrl: String?,
    var isChosen: Boolean = false,
) : Parcelable
