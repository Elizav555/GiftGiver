package com.example.giftgiver.features.start.domain

import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun login() = authRepository.clientLogin()

    fun logout() = authRepository.clientLogout()

    fun restart() = authRepository.restart()
}
