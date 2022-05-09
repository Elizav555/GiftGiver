package com.example.giftgiver.features.wishlist.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.giftgiver.features.client.data.room.ClientR
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "wishlists", foreignKeys = [ForeignKey(
        entity = ClientR::class,
        parentColumns = arrayOf("vkId"),
        childColumns = arrayOf("client_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
@Parcelize
data class WishlistR(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "client_id", index = true) val clientId: Long,
    val name: String = "",
    var giftsIds: List<String> = listOf(),
) : Parcelable
