package com.example.giftgiver.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class UserInfo(
    val id: Int = 0,
    val bdate: String = "Unknown",
    val interests: String = "Unknown",
    val music: String = "Unknown",
    val movies: String = "Unknown",
    val tv: String = "Unknown",
    val books: String = "Unknown",
    val games: String = "Unknown",
    val about: String = "Unknown",
) : Parcelable {
    fun parse(json: JSONObject) = UserInfo(
        id = json.optInt("id", 0),
        bdate = json.optString("bdate", ""),
        interests = json.optString("interests", ""),
        music = json.optString("music", ""),
        movies = json.optString("movies", ""),
        tv = json.optString("tv", ""),
        books = json.optString("books", ""),
        games = json.optString("games", ""),
        about = json.optString("about", ""),
    )
}
