package com.example.giftgiver.features.start.data

import com.example.giftgiver.features.start.domain.AuthRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class AuthRepositoryImpl : AuthRepository {
    private var isAuth: Boolean? = null
    private val isAuthenticated: MutableSharedFlow<Boolean?> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun isAuth(): SharedFlow<Boolean?> = isAuthenticated

    override fun clientLogin() = update(true)

    override fun clientLogout() = update(false)

    override fun restart() = update()

    private fun update(value: Boolean? = null) {
        isAuth = value
        isAuthenticated.tryEmit(isAuth)
    }
}
