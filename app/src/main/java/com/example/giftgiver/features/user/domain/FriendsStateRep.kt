package com.example.giftgiver.features.user.domain

import kotlinx.coroutines.flow.SharedFlow

interface FriendsStateRep {
    fun addFriends(friends: List<UserInfo>)
    fun deleteFriends()
    fun getFriends(): SharedFlow<List<UserInfo>>
}
