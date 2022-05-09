package com.example.giftgiver.features.event.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.giftgiver.features.client.data.room.ClientR
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(
    tableName = "events", foreignKeys = [ForeignKey(
        entity = ClientR::class,
        parentColumns = arrayOf("vkId"),
        childColumns = arrayOf("client_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class EventR(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "client_id", index = true) val clientId: Long,
    val date: Date = Date(),
    val desc: String? = "",
) : Parcelable

