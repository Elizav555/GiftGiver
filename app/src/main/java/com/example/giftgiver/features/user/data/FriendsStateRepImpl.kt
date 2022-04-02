package com.example.giftgiver.features.user.data

import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.example.giftgiver.features.user.domain.UserInfo

class FriendsStateRepImpl : FriendsStateRep {
    private var curFriends: List<UserInfo> = listOf()
    override fun addFriends(friends: List<UserInfo>) {
        curFriends = friends
    }

    override fun deleteFriends() {
        curFriends = listOf()
    }

    override fun getFriends(): List<UserInfo> = curFriends
}
