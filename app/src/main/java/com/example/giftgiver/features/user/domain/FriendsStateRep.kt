package com.example.giftgiver.features.user.domain

import com.example.giftgiver.features.user.domain.UserInfo

interface FriendsStateRep {
    fun addFriends(friends: List<UserInfo>)
    fun deleteFriends()
    fun getFriends(): List<UserInfo>
}
