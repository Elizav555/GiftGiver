package com.example.giftgiver.features.client.data.room

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.user.data.room.UserInfoR
import com.example.giftgiver.features.wishlist.data.room.WishlistR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "clients")
@Parcelize
data class ClientR(
    @PrimaryKey(autoGenerate = false) val vkId: Long = 0,
    val favFriendsIds: List<Long> = listOf(),
    @Embedded
    val info: UserInfoR,
    val events: List<EventR> = listOf(),
    val giftsInfo: List<GiftInfoR> = listOf(),
    val wishlists: List<WishlistR> = listOf(),
) : Parcelable
