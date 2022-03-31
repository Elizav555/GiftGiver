package com.example.giftgiver.features.user.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.user.domain.useCases.UpdateFavFriendsUseCase
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val updateFavFriends: UpdateFavFriendsUseCase
) : ViewModel() {
    private val client = ClientState.client
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
                clientFriend?.let { friend -> it.favFriends.add(friend) }
            } else clientFriend?.let { friend -> it.favFriends.remove(friend) }
            updateFavFriends(client.vkId, client.favFriends)
            ClientState.client?.favFriends = client.favFriends
        }
    }
}
