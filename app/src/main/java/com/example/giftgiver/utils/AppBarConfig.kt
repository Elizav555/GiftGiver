package com.example.giftgiver.utils

data class AppBarConfig(
    var firstButton: AppBarButton? = null,
    val secondButton: AppBarButton? = null,
    var title: String? = null,
    val hasSearch: Boolean = false
)