package com.example.giftgiver.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class OnAppBarChangesListenerImpl : OnAppBarChangesListener {
    private val _appBarConfig =
        MutableSharedFlow<AppBarConfig>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    private var curConfig: AppBarConfig = AppBarConfig()
    private val _appBarTitle =
        MutableSharedFlow<String>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        _appBarConfig.tryEmit(curConfig)
    }

    override fun onToolbarChanges(appBarConfig: AppBarConfig) {
        curConfig = appBarConfig
        _appBarConfig.tryEmit(curConfig)
    }

    override fun onTitleChanges(title: String) {
        curConfig.title = title
        curConfig.title?.let { _appBarTitle.tryEmit(it) }
    }

    override fun observeToolbarChanges(): SharedFlow<AppBarConfig> = _appBarConfig
    override fun observeTitleChanges(): SharedFlow<String> = _appBarTitle
}