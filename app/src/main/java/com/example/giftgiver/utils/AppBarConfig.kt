package com.example.giftgiver.utils

data class AppBarConfig(
    val firstButton: AppBarButton? = null,
    val secondButton: AppBarButton? = null,
    var title: String? = null,
    val hasSearch: Boolean = false
)