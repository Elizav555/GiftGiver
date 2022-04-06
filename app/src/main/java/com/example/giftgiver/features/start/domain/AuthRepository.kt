package com.example.giftgiver.features.start.domain

import androidx.lifecycle.LiveData

interface AuthRepository {
    fun isAuth(): LiveData<Boolean?>
    fun clientLogin()
    fun clientLogout()
    fun restart()
}
