package com.example.giftgiver.features.user.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val clientFBUseCase: ClientFBUseCase,
    getClientState: GetClientStateUseCase,
) : ViewModel() {
    private val client = getClientState()
    private var _friend: MutableLiveData<Result<Client?>> = MutableLiveData()
    val friend: LiveData<Result<Client?>> = _friend
    private var clientFriend: Client? = null
    fun getFriend(vkId: Long) = viewModelScope.launch {
        try {
            clientFriend = getClientByVkId(vkId)
            _friend.value = Result.success(clientFriend)
        } catch (ex: Exception) {
            _friend.value = Result.failure(ex)
        }
    }

    fun updateFavFriends(isFav: Boolean) = viewModelScope.launch {
        client?.let {
            if (isFav) {
                clientFriend?.let { friend -> it.favFriendsIds.add(friend.vkId) }
            } else clientFriend?.let { friend -> it.favFriendsIds.remove(friend.vkId) }
            clientFBUseCase.updateFavFriends(client.vkId, it.favFriendsIds)
            client.favFriendsIds = it.favFriendsIds
        }
    }

    fun checkIsFav() = client?.favFriendsIds?.contains(clientFriend?.vkId)
}
