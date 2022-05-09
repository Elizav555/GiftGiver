package com.example.giftgiver.common.db.room.converters

import androidx.room.TypeConverter
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
}
