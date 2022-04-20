package com.example.giftgiver.features.start.domain

import kotlinx.coroutines.flow.SharedFlow

interface AuthRepository {
    fun isAuth(): SharedFlow<Boolean?>
    fun clientLogin()
    fun clientLogout()
    fun restart()
}
