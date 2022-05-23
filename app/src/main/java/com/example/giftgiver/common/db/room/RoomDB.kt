package com.example.giftgiver.common.db.room

import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.giftgiver.common.db.room.converters.DateConverter
import com.example.giftgiver.common.db.room.converters.ListsConverter
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.data.room.ClientR
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.gift.data.room.GiftR
import com.example.giftgiver.features.wishlist.data.room.WishlistR

@TypeConverters(DateConverter::class, ListsConverter::class)
@Database(
    entities = [
        ClientR::class,
        GiftR::class,
        GiftInfoR::class,
        EventR::class,
        WishlistR::class
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 3, to = 4, spec = RoomDB.MyExampleAutoMigration::class),
        AutoMigration(from = 4, to = 5)
    ]
)


abstract class RoomDB : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun giftDao(): GiftDao

    companion object {
        const val DATABASE_NAME = "gift_giver.db"
    }

    @DeleteColumn(
        tableName = "clients",
        columnName = "giftsInfo"
    )
    @DeleteColumn(
        tableName = "clients",
        columnName = "wishlists"
    )
    @DeleteColumn(
        tableName = "clients",
        columnName = "events"
    )
    class MyExampleAutoMigration : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            // Invoked once auto migration is done
        }
    }
}
