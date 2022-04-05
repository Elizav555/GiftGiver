package com.example.giftgiver.features.client.domain.useCases

import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.example.giftgiver.features.user.domain.UserInfo
import javax.inject.Inject

class GetClientStateUseCase @Inject constructor(
    private val clientStateRep: ClientStateRep,
    private val friendsStateRep: FriendsStateRep
) {
    operator fun invoke() = clientStateRep.getClient()

    fun getFriendsState() = friendsStateRep.getFriends()

    fun addFriends(friends: List<UserInfo>) = friendsStateRep.addFriends(friends)
}
