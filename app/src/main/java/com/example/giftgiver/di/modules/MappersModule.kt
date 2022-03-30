package com.example.giftgiver.di.modules

import com.example.giftgiver.data.mappers.DateMapper
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.data.mappers.VkMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MappersModule {
    @Provides
    @Singleton
    fun provideHolidayMapper(): HolidayMapper = HolidayMapper()

    @Provides
    @Singleton
    fun provideFBMapper(): FBMapper = FBMapper()

    @Provides
    @Singleton
    fun provideDateMapper(): DateMapper = DateMapper()

    @Provides
    @Singleton
    fun provideVkMapper(): VkMapper = VkMapper()
}
