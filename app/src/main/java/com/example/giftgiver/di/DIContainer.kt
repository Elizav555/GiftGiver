package com.example.giftgiver.di


import androidx.viewbinding.BuildConfig
import com.example.giftgiver.data.holidaysApi.HolidayApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val DOMAIN = "https://date.nager.at/"

class DIContainer {
    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                }
            }
            .build()
    }

    val api: HolidayApi by lazy {
        Retrofit.Builder()
            .baseUrl(DOMAIN)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HolidayApi::class.java)
    }
}
