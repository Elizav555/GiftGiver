package com.example.giftgiver.features.start.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.AddClientUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsVK
import com.example.giftgiver.utils.FriendsState
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val loadFriendsVK: LoadFriendsVK,
    private val addClientUseCase: AddClientUseCase
) : ViewModel() {

    private var _client: MutableLiveData<Result<Client?>> = MutableLiveData()
    val client: LiveData<Result<Client?>> = _client

    private var _friends: MutableLiveData<Result<List<UserInfo>>> = MutableLiveData()
    val friends: LiveData<Result<List<UserInfo>>> = _friends

    fun getClient(vkId: Long) = viewModelScope.launch {
        try {
            val clientReceived = getClientByVkId(vkId)
            _client.value = Result.success(clientReceived)
        } catch (ex: Exception) {
            _client.value = Result.failure(ex)
        }
    }

    fun addClient(clientToAdd: Client) = viewModelScope.launch {
        addClientUseCase(clientToAdd)
    }

    fun loadFriends() = viewModelScope.launch {
        try {
            val friendsReceived = loadFriendsVK.loadFriends(VK.getUserId().value)
            FriendsState.friends = friendsReceived
            _friends.value = Result.success(friendsReceived)
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }
}
