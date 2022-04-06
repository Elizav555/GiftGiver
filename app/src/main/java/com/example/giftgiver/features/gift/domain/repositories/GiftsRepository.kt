package com.example.giftgiver.features.gift.domain.repositories

import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.wishlist.domain.Wishlist

interface GiftsRepository {
    suspend fun addGift(clientId: Long, gift: Gift, wishlists: List<Wishlist>): String
    suspend fun deleteGift(clientId: Long, gift: Gift, wishlists: List<Wishlist>)
    suspend fun updateGift(clientId: Long, gift: Gift)
    suspend fun getGift(clientId: Long, giftId: String): Gift?
}
