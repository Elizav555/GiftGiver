package com.example.giftgiver.common.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.giftgiver.common.db.room.converters.DateConverter
import com.example.giftgiver.common.db.room.converters.ListsConverter
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.data.room.ClientR
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.gift.data.room.GiftR

@TypeConverters(DateConverter::class, ListsConverter::class)
@Database(
    entities = [
        ClientR::class,
        GiftR::class
    ],
    version = 3
)
abstract class RoomDB : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun giftDao(): GiftDao

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
