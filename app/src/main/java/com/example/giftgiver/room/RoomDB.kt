package com.example.giftgiver.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.giftgiver.room.converters.DateConverter
import com.example.giftgiver.room.dao.*
import com.example.giftgiver.room.entities.*

@TypeConverters(DateConverter::class)
@Database(
    entities = [
        CalendarR::class, CartR::class, ClientR::class, EventR::class,
        GiftR::class, UserInfoR::class, UserR::class, WishlistR::class
    ],
    version = 1
)
abstract class RoomDB : RoomDatabase() {

    abstract fun calendarDao(): CalendarDao
    abstract fun cartDao(): CartDao
    abstract fun clientDao(): ClientDao
    abstract fun eventDao(): EventDao
    abstract fun giftDao(): GiftDao
    abstract fun userDao(): UserDao
    abstract fun userInfoDao(): UserInfoDao
    abstract fun wishlistDao(): WishlistDao

    companion object {

        private const val DATABASE_NAME = "gift_giver.db"

        @Volatile
        private var instance: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RoomDB::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
