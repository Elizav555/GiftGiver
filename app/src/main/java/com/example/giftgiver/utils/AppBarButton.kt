package com.example.giftgiver.utils

import androidx.annotation.DrawableRes

data class AppBarButton(
    @DrawableRes val icon: Int,
    val action: () -> Unit,
)