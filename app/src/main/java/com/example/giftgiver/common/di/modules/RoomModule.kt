package com.example.giftgiver.common.di.modules

import android.content.Context
import androidx.room.Room
import com.example.giftgiver.common.db.room.RoomDB
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.gift.data.room.GiftDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context) = Room.databaseBuilder(
        context,
        RoomDB::class.java,
        RoomDB.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideClientDao(database: RoomDB): ClientDao = database.clientDao()

    @Provides
    @Singleton
    fun provideGiftDao(database: RoomDB): GiftDao = database.giftDao()
}
