package com.example.giftgiver.features.calendar.data.holidaysApi

import com.example.giftgiver.features.calendar.data.holidaysApi.response.Holiday
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApi {
    @GET("/api/v3/publicholidays/{year}/RU")
    suspend fun getHolidays(@Path("year") year: String?): List<Holiday>
}
