package com.example.giftgiver.common.di.modules

import android.app.Application
import android.content.Context
import com.example.giftgiver.utils.OnAppBarChangesListener
import com.example.giftgiver.utils.OnAppBarChangesListenerImpl
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    fun provideAppBarChangesListener(): OnAppBarChangesListener = OnAppBarChangesListenerImpl()
}
