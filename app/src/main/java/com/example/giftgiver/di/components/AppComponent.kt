package com.example.giftgiver.di.components

import android.content.Context
import com.example.giftgiver.di.modules.AppModule
import com.example.giftgiver.di.modules.DataModule
import com.example.giftgiver.di.modules.MappersModule
import com.example.giftgiver.di.modules.NetModule
import com.example.giftgiver.domain.repositories.ClientsRepository
import com.example.giftgiver.domain.repositories.HolidayRepository
import com.example.giftgiver.domain.repositories.ImageStorage
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, NetModule::class, MappersModule::class])
interface AppComponent {

    fun getContext(): Context

    fun getDefaultDispatcher(): CoroutineDispatcher

    fun getHolidayRepository(): HolidayRepository

    fun getClientsRepository(): ClientsRepository

    fun getImageStorage(): ImageStorage
}
