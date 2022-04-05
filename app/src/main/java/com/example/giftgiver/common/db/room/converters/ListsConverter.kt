package com.example.giftgiver.common.db.room.converters

import androidx.room.TypeConverter
import com.example.giftgiver.features.event.data.room.EventR
import com.example.giftgiver.features.gift.data.room.GiftInfoR
import com.example.giftgiver.features.wishlist.data.room.WishlistR
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListsConverter {
    @TypeConverter
    fun restoreStringList(listOfString: String?): List<String?>? {
        return Gson().fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun saveStringList(listOfString: List<String?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun restoreLongList(listOfLong: String?): List<Long?>? {
        return Gson().fromJson(listOfLong, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun saveLongList(listOfLong: List<Long?>?): String? {
        return Gson().toJson(listOfLong)
    }

    @TypeConverter
    fun fromEventRList(value: List<EventR>): String {
        val gson = Gson()
        val type = object : TypeToken<List<EventR>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toEventRList(value: String): List<EventR> {
        val gson = Gson()
        val type = object : TypeToken<List<EventR>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromGiftInfoRList(value: List<GiftInfoR>): String {
        val gson = Gson()
        val type = object : TypeToken<List<GiftInfoR>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGiftInfoRList(value: String): List<GiftInfoR> {
        val gson = Gson()
        val type = object : TypeToken<List<GiftInfoR>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWishlistRList(value: List<WishlistR>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WishlistR>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWishlistRList(value: String): List<WishlistR> {
        val gson = Gson()
        val type = object : TypeToken<List<WishlistR>>() {}.type
        return gson.fromJson(value, type)
    }
}
