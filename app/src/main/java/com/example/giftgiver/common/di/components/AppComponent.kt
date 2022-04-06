package com.example.giftgiver.common.di.components

import android.app.Application
import com.example.giftgiver.App
import com.example.giftgiver.common.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        NetModule::class,
        MappersModule::class,
        RoomModule::class,
        ViewModelsModule::class,
        UIModule::class,
        AndroidInjectionModule::class,
    ]
)
interface AppComponent : AndroidInjector<App> {
    override fun inject(instance: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}
