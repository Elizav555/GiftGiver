package com.example.giftgiver.di.modules

import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.ImageStorageImpl
import com.example.giftgiver.data.holidaysApi.HolidayApi
import com.example.giftgiver.data.holidaysApi.HolidayRepositoryImpl
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.example.giftgiver.domain.repositories.HolidayRepository
import com.example.giftgiver.domain.repositories.ImageStorage
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
        fbMapper: FBMapper
    ): ClientsRepository = ClientsRepositoryImpl(fbMapper)

    @Provides
    @Singleton
    fun provideImageStorage(): ImageStorage = ImageStorageImpl()
}
