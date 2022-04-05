package com.example.giftgiver.features.user.domain

interface FriendsStateRep {
    fun addFriends(friends: List<UserInfo>)
    fun deleteFriends()
    fun getFriends(): List<UserInfo>
}
