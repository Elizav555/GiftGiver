package com.example.giftgiver.common.di.modules

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.calendar.data.mappers.HolidayMapper
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.user.data.vk.VkMapper
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVK
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class MappersModule {
    @Provides
    @Singleton
    fun provideHolidayMapper(dateMapper: DateMapper): HolidayMapper = HolidayMapper(dateMapper)

    @Provides
    @Singleton
    fun provideFBMapper(
        dispatcher: CoroutineDispatcher,
        loadUserInfoVK: LoadUserInfoVK
    ): FBMapper =
        FBMapper(dispatcher, loadUserInfoVK)

    @Provides
    @Singleton
    fun provideDateMapper(): DateMapper = DateMapper()

    @Provides
    @Singleton
    fun provideVkMapper(): VkMapper = VkMapper()

    @Provides
    @Singleton
    fun provideRoomMapper(): RoomMapper = RoomMapper()
}
