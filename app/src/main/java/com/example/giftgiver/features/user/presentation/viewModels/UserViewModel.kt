package com.example.giftgiver.features.user.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBInteractor
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val clientFBInteractor: ClientFBInteractor,
    private val getClientState: GetClientStateUseCase,
) : ViewModel() {
    private var _friend: MutableLiveData<Result<Client?>> = MutableLiveData()
    val friend: LiveData<Result<Client?>> = _friend
    private var clientFriend: Client? = null
    private var client: Client? = null

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
                checkIsFav()
            }
        }
    }

    fun getFriend(vkId: Long) = viewModelScope.launch {
        try {
            clientFriend = getClientByVkId(vkId)
            _friend.value = Result.success(clientFriend)
        } catch (ex: Exception) {
            _friend.value = Result.failure(ex)
        }
    }

    fun updateFavFriends(isFav: Boolean) = viewModelScope.launch {
        client?.let { client ->
            if (isFav) {
                clientFriend?.let { friend -> client.favFriendsIds.add(friend.vkId) }
            } else clientFriend?.let { friend -> client.favFriendsIds.remove(friend.vkId) }
            clientFBInteractor.updateFavFriends(client.vkId, client.favFriendsIds)
        }
    }

    fun checkIsFav() = client?.favFriendsIds?.contains(clientFriend?.vkId)
}
