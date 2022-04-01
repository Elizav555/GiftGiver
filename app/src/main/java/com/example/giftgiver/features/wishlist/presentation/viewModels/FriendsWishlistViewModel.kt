package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsWishlistViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val updateWishlists: UpdateWishlistUseCase,
    private val updateCart: UpdateCartUseCase
) : ViewModel() {
    private val client = ClientState.client
    private var _friend: MutableLiveData<Result<Client?>> = MutableLiveData()
    val friend: LiveData<Result<Client?>> = _friend
    private var curFriend: Client? = null

    fun getFriend(vkId: Long) = viewModelScope.launch {
        try {
            curFriend = getClientByVkId(vkId)
            _friend.value = Result.success(curFriend)
        } catch (ex: Exception) {
            _friend.value = Result.failure(ex)
        }
    }

    fun checkedFunc(pos: Int, isChecked: Boolean, wishlistIndex: Int) = curFriend?.let { friend ->
        try {
            friend.wishlists[wishlistIndex].gifts[pos].isChosen = isChecked
            val gift = friend.wishlists[wishlistIndex].gifts[pos]
            if (isChecked) {
                client?.cart?.gifts?.add(gift)
            } else {
                gift.isChosen = true
                client?.cart?.gifts?.remove(gift)
                gift.isChosen = false
            }
            viewModelScope.launch {
                updateWishlists(friend.vkId, friend.wishlists)
                client?.let {
                    updateCart(it.vkId, it.cart.gifts)
                    ClientState.client?.cart?.gifts = it.cart.gifts
                }
            }
            _friend.value = Result.success(curFriend)
        } catch (ex: Exception) {
            _friend.value = Result.failure(ex)
        }
    }
}
