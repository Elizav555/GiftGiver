package com.example.giftgiver.features.gift.domain

interface GiftsRepOffline {

    suspend fun addGift(gift: Gift)

    suspend fun deleteGift(gift: Gift)

    suspend fun getGift(giftId: String): Gift?
}
