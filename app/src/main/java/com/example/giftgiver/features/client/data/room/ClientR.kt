package com.example.giftgiver.features.client.data.room

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.user.data.room.UserInfoR
import com.example.giftgiver.features.wishlist.data.room.WishlistR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "clients")
@Parcelize
data class ClientR(
    @PrimaryKey(autoGenerate = false) val vkId: Long = 0,
    val favFriendsIds: List<String> = listOf(),
    @Embedded
    val info: UserInfoR
) : Parcelable

@Parcelize
data class ClientFullR(
    @Embedded
    val client: ClientR,
    @Relation(
        parentColumn = "vkId",
        entityColumn = "client_id"
    )
    val events: List<EventR> = listOf(),
    @Relation(
        parentColumn = "vkId",
        entityColumn = "client_id"
    )
    val giftsInfo: List<GiftInfoR> = listOf(),
    @Relation(
        parentColumn = "vkId",
        entityColumn = "client_id"
    )
    val wishlists: List<WishlistR> = listOf(),
) : Parcelable