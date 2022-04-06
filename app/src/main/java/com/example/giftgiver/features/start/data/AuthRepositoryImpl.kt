package com.example.giftgiver.features.start.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.giftgiver.features.start.domain.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    private var isAuth: Boolean? = null
    private val isAuthenticated: MutableLiveData<Boolean?> = MutableLiveData(isAuth)

    override fun isAuth(): LiveData<Boolean?> = isAuthenticated

    override fun clientLogin() = update(true)

    override fun clientLogout() = update(false)

    override fun restart() = update()

    private fun update(value: Boolean? = null) {
        isAuth = value
        isAuthenticated.postValue(isAuth)
    }
}
