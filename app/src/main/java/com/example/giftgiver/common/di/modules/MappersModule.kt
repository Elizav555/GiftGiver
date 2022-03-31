package com.example.giftgiver.common.di.modules

import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.calendar.data.mappers.HolidayMapper
import com.example.giftgiver.features.user.data.vk.VkMapper
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
