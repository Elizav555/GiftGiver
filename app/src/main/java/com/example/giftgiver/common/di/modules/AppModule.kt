package com.example.giftgiver.common.di.modules

import android.app.Application
import android.content.Context
import com.example.giftgiver.utils.AppDispatchers
import com.example.giftgiver.utils.OnAppBarChangesListener
import com.example.giftgiver.utils.OnAppBarChangesListenerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideAppBarChangesListener(): OnAppBarChangesListener = OnAppBarChangesListenerImpl()
}
