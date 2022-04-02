package com.example.giftgiver.common.di.components

import android.content.Context
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.common.di.modules.AppModule
import com.example.giftgiver.common.di.modules.DataModule
import com.example.giftgiver.common.di.modules.MappersModule
import com.example.giftgiver.common.di.modules.NetModule
import com.example.giftgiver.features.calendar.domain.HolidayRepository
import com.example.giftgiver.features.client.data.fb.FBMapper
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.client.domain.ClientsRepository
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.user.data.vk.VkMapper
import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.google.firebase.firestore.FirebaseFirestore
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

    fun getClientStateRepository(): ClientStateRep

    fun getFriendsStateRepository(): FriendsStateRep

    fun getImageStorage(): ImageStorage

    fun getDateMapper(): DateMapper

    fun getFBMapper(): FBMapper

    fun getVKMapper(): VkMapper

    fun getFirestore(): FirebaseFirestore
}
