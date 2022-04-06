package com.example.giftgiver

import android.app.Application
import android.util.Log
import com.example.giftgiver.common.di.components.DaggerAppComponent
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            Log.e("vk", "token expired")
            MainActivity.startFrom(this@App)
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
