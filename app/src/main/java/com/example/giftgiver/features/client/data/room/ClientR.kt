package com.example.giftgiver.features.client.data.room

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.giftgiver.features.calendar.data.room.CalendarR
import com.example.giftgiver.features.cart.data.room.CartR
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
) : Parcelable

data class ClientAndCalendar(
    @Embedded val clientR: ClientR,
    @Relation(
        parentColumn = "vkId",
        entityColumn = "clientId"
    )
    val calendar: CalendarR,
)

data class ClientAndCart(
    @Embedded val clientR: ClientR,
    @Relation(
        parentColumn = "vkId",
        entityColumn = "clientId"
    )
    val cart: CartR,
)

data class ClientWithWishlists(
    @Embedded val clientR: ClientR,
    @Relation(
        parentColumn = "vkId",
        entityColumn = "clientId"
    )
    val wishlists: List<WishlistR>
)
