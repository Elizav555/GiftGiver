package com.example.giftgiver

import android.app.Application
import com.example.giftgiver.common.di.components.AppComponent
import com.example.giftgiver.common.di.components.DaggerAppComponent
import com.example.giftgiver.common.di.components.DaggerMainComponent
import com.example.giftgiver.common.di.components.MainComponent
import com.example.giftgiver.common.di.modules.AppModule
import com.example.giftgiver.common.di.modules.DataModule
import com.example.giftgiver.common.di.modules.MappersModule
import com.example.giftgiver.common.di.modules.NetModule
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dataModule(DataModule())
            .mappersModule(MappersModule())
            .netModule(NetModule())
            .build()

        mainComponent = DaggerMainComponent.builder()
            .appComponent(appComponent)
            .build()

        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            //todo handle token inspiration
        }
    }

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var mainComponent: MainComponent
    }
}
