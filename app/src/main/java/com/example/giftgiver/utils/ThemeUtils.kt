package com.example.giftgiver.utils

import androidx.appcompat.app.AppCompatDelegate

object ThemeUtils {
    var isLight = true
        set(value) {
            field = value
            if (isLight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
}