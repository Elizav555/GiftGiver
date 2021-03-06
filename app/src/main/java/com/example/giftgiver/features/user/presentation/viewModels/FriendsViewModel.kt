package com.example.giftgiver.features.user.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.user.domain.UserInfo
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    private val getClientState: GetClientStateUseCase
) : ViewModel() {
    private var _friends: MutableLiveData<Result<List<UserInfo>>> = MutableLiveData()
    val friends: LiveData<Result<List<UserInfo>>> = _friends
    private var client: Client? = null
    private var curFriends = listOf<UserInfo>()

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
                getFriends()
            }
        }
        viewModelScope.launch {
            getClientState.getFriendsState().collect {
                curFriends = it
                getFriends()
            }
        }
    }

    fun getFriends() = viewModelScope.launch {
        try {
            _friends.value = Result.success(curFriends)
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }

    fun filterFriends(isFav: Boolean) = viewModelScope.launch {
        try {
            if (isFav) {
                client?.let { client ->
                    val friendsFiltered =
                        curFriends.filter { client.favFriendsIds.contains(it.vkId) }
                            .sortedBy { friend -> friend.name }
                    _friends.value = Result.success(friendsFiltered)
                }
            } else getFriends()
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }
}
