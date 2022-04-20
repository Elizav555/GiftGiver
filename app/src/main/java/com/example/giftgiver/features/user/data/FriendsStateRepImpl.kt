package com.example.giftgiver.features.user.data

import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.example.giftgiver.features.user.domain.UserInfo
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FriendsStateRepImpl : FriendsStateRep {
    private var curFriends: List<UserInfo> = listOf()
    private val _friendsFlow =
        MutableSharedFlow<List<UserInfo>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        _friendsFlow.tryEmit(curFriends)
    }

    override fun addFriends(friends: List<UserInfo>) {
        curFriends = friends
        _friendsFlow.tryEmit(curFriends)
    }

    override fun deleteFriends() {
        curFriends = listOf()
        _friendsFlow.tryEmit(curFriends)
    }

    override fun getFriends(): SharedFlow<List<UserInfo>> = _friendsFlow
}
