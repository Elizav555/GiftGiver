package com.example.giftgiver.features.client.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.giftgiver.features.user.data.room.UserInfoR
import com.example.giftgiver.features.wishlist.data.room.WishlistR
import com.example.giftgiver.features.calendar.data.room.CalendarR
import com.example.giftgiver.features.cart.data.room.CartR
import kotlinx.parcelize.Parcelize

@Entity(tableName = "clients")
@Parcelize
data class ClientR(
    @PrimaryKey(autoGenerate = false) val vkId: Long? = 0,
    val calendar: CalendarR,
    val cart: CartR,
    val favFriendsIds: List<Long> = listOf(),
    val wishlists: List<WishlistR> = listOf(),
    val info: UserInfoR,
) : Parcelable
