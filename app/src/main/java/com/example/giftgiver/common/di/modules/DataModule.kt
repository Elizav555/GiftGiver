package com.example.giftgiver.common.di.modules

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.calendar.data.holidaysApi.HolidayApi
import com.example.giftgiver.features.calendar.data.holidaysApi.HolidayRepositoryImpl
import com.example.giftgiver.features.calendar.data.mappers.HolidayMapper
import com.example.giftgiver.features.calendar.domain.HolidayRepository
import com.example.giftgiver.features.client.data.ClientStateRepImpl
import com.example.giftgiver.features.client.data.ClientsRepOfflineImpl
import com.example.giftgiver.features.client.data.fb.ClientsRepositoryImpl
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.client.data.room.ClientDao
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.client.domain.repositories.ClientsRepOffline
import com.example.giftgiver.features.client.domain.repositories.ClientsRepository
import com.example.giftgiver.features.gift.data.GiftsRepOfflineImpl
import com.example.giftgiver.features.gift.data.GiftsRepositoryImpl
import com.example.giftgiver.features.gift.data.room.GiftDao
import com.example.giftgiver.features.gift.domain.repositories.GiftsRepOffline
import com.example.giftgiver.features.gift.domain.repositories.GiftsRepository
import com.example.giftgiver.features.images.data.ImageStorageImpl
import com.example.giftgiver.features.images.domain.ImageStorage
import com.example.giftgiver.features.start.data.AuthRepositoryImpl
import com.example.giftgiver.features.start.domain.AuthRepository
import com.example.giftgiver.features.user.data.FriendsStateRepImpl
import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {
    @Provides
    @Singleton
    fun provideHolidayRepository(
        api: HolidayApi,
        holidayMapper: HolidayMapper,
    ): HolidayRepository {
        return HolidayRepositoryImpl(
            api = api,
            holidayMapper = holidayMapper
        )
    }

    @Provides
    @Singleton
    fun provideClientsRepository(
        fbMapper: FBMapper,
        firestore: FirebaseFirestore
    ): ClientsRepository = ClientsRepositoryImpl(fbMapper, firestore)

    @Provides
    @Singleton
    fun provideClientsOfflineRepository(
        clientDao: ClientDao,
        roomMapper: RoomMapper
    ): ClientsRepOffline =
        ClientsRepOfflineImpl(clientDao, roomMapper)

    @Provides
    @Singleton
    fun provideGiftsOfflineRepository(
        giftDao: GiftDao,
        roomMapper: RoomMapper
    ): GiftsRepOffline = GiftsRepOfflineImpl(giftDao, roomMapper)

    @Provides
    @Singleton
    fun provideGiftsRepository(
        fbMapper: FBMapper,
        firestore: FirebaseFirestore
    ): GiftsRepository = GiftsRepositoryImpl(fbMapper, firestore)

    @Provides
    @Singleton
    fun provideImageStorage(storageReference: StorageReference): ImageStorage =
        ImageStorageImpl(storageReference)

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()

    @Provides
    @Singleton
    fun provideClientStateRep(): ClientStateRep = ClientStateRepImpl()

    @Provides
    @Singleton
    fun provideFriendsStateRep(): FriendsStateRep = FriendsStateRepImpl()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorageReference(storage: FirebaseStorage): StorageReference = storage.reference
}
