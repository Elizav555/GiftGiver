package com.example.giftgiver.common.di.modules

import androidx.viewbinding.BuildConfig
import com.example.giftgiver.features.calendar.data.holidaysApi.HolidayApi
import com.example.giftgiver.features.user.data.vk.VKFriendsRequest
import com.example.giftgiver.features.user.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.features.user.data.vk.VkMapper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {
    @Provides
    @Singleton
    fun provideHolidayApi(retrofit: Retrofit): HolidayApi = retrofit
        .create(HolidayApi::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttp: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(DOMAIN)
        .client(okhttp)
        .addConverterFactory(converterFactory)
        .build()

    @Provides
    @Singleton
    fun provideClient() = OkHttpClient.Builder()
        .also {
            if (BuildConfig.DEBUG) {
                it.addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    companion object {
        private const val DOMAIN = "https://date.nager.at/"
    }

//    @Provides
//    @Singleton
//    fun provideVKUserWithInfoRequest(id: Long?, vkMapper: VkMapper) =
//        VKUserWithInfoRequest(id, vkMapper)
//
//    @Provides
//    @Singleton
//    fun provideVKFriendsRequest(id: Long?, vkMapper: VkMapper) =
//        VKFriendsRequest(id, vkMapper)
}
