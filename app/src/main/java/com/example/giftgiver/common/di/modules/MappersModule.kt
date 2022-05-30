package com.example.giftgiver.common.di.modules

import com.example.giftgiver.common.db.room.RoomMapper
import com.example.giftgiver.features.calendar.data.mappers.HolidayMapper
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.user.data.vk.VkMapper
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVKUseCase
import com.example.giftgiver.utils.AppDispatchers
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MappersModule {
    @Provides
    @Singleton
    fun provideHolidayMapper(dateMapper: DateMapper): HolidayMapper = HolidayMapper(dateMapper)

    @Provides
    @Singleton
    fun provideFBMapper(
        dispatcher: AppDispatchers,
        loadUserInfoVKUseCase: LoadUserInfoVKUseCase
    ): FBMapper =
        FBMapper(dispatcher, loadUserInfoVKUseCase)

    @Provides
    @Singleton
    fun provideDateMapper(): DateMapper = DateMapper()

    @Provides
    @Singleton
    fun provideVkMapper(dateMapper: DateMapper): VkMapper = VkMapper(dateMapper)

    @Provides
    @Singleton
    fun provideRoomMapper(): RoomMapper = RoomMapper()
}
