package com.example.giftgiver.features.user.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId
) : ViewModel() {
    private val client = ClientState.client
    private var _friends: MutableLiveData<Result<List<UserInfo>>> = MutableLiveData()
    val friends: LiveData<Result<List<UserInfo>>> = _friends
    fun getFriends() = viewModelScope.launch {
        try {
            _friends.value = Result.success(FriendsState.friends)
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }

    fun filterFriends(isFav: Boolean) = viewModelScope.launch {
        try {
            if (isFav) {
                client?.let { client ->
                    val friendsFiltered =
                        client.favFriendsIds.mapNotNull { getClientByVkId(it)?.info }
                            .sortedBy { friend -> friend.name }
                    _friends.value = Result.success(friendsFiltered)
                }
            } else getFriends()
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }
}
