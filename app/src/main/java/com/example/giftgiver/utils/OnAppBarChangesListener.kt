package com.example.giftgiver.utils

import kotlinx.coroutines.flow.SharedFlow

interface OnAppBarChangesListener {
    fun onToolbarChanges(appBarConfig: AppBarConfig)
    fun onTitleChanges(title: String)
    fun observeToolbarChanges(): SharedFlow<AppBarConfig>
    fun observeTitleChanges(): SharedFlow<String>
}