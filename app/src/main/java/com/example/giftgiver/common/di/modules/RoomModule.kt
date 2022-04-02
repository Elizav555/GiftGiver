package com.example.giftgiver.common.di.modules

import android.content.Context
import com.example.giftgiver.common.db.room.RoomDB
import com.example.giftgiver.features.calendar.data.room.CalendarDao
import com.example.giftgiver.features.cart.data.room.CartDao
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.event.data.room.EventDao
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.user.data.room.UserInfoDao
import com.example.giftgiver.features.wishlist.data.room.WishlistDao
import dagger.Provides
import javax.inject.Singleton

class RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): RoomDB {
        return RoomDB.invoke(context)
    }

    @Provides
    @Singleton
    fun provideCalendarDao(database: RoomDB): CalendarDao = database.calendarDao()

    @Provides
    @Singleton
    fun provideCartDao(database: RoomDB): CartDao = database.cartDao()

    @Provides
    @Singleton
    fun provideClientDao(database: RoomDB): ClientDao = database.clientDao()

    @Provides
    @Singleton
    fun provideEventDao(database: RoomDB): EventDao = database.eventDao()

    @Provides
    @Singleton
    fun provideGiftDao(database: RoomDB): GiftDao = database.giftDao()

    @Provides
    @Singleton
    fun provideUserInfoDao(database: RoomDB): UserInfoDao = database.userInfoDao()

    @Provides
    @Singleton
    fun provideWishlistDao(database: RoomDB): WishlistDao = database.wishlistDao()
}
