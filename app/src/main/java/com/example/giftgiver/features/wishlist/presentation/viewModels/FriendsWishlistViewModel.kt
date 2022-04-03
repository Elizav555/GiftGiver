package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.gift.domain.useCases.GetGiftUseCase
import com.example.giftgiver.features.gift.domain.useCases.UpdateGiftUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsWishlistViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val updateCart: UpdateCartUseCase,
    private val clientStateRep: ClientStateRep,
    private val getGiftUseCase: GetGiftUseCase,
    private val updateGiftUseCase: UpdateGiftUseCase
) : ViewModel() {
    private val client = clientStateRep.getClient()
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
        viewModelScope.launch {
            try {
                val curGift =
                    getGiftUseCase(friend.vkId, friend.wishlists[wishlistIndex].giftsIds[pos])
                curGift?.let { gift ->
                    gift.isChosen = isChecked
                    if (isChecked) {
                        client?.cart?.giftsInfo?.add(GiftInfo(gift.id, gift.forId))
                    } else {
                        val giftToDelete =
                            client?.cart?.giftsInfo?.firstOrNull { giftInfo -> giftInfo.giftId == gift.id }
                        client?.cart?.giftsInfo?.remove(giftToDelete)
                    }
                    client?.let {
                        updateCart(it.vkId, it.cart.giftsInfo)
                        clientStateRep.addClient(client)
                        updateGiftUseCase(friend.vkId, gift)
                    }
                    _friend.value = Result.success(curFriend)
                }
            } catch (ex: Exception) {
                _friend.value = Result.failure(ex)
            }
        }
    }

    fun getClientCart(): List<GiftInfo> = client?.cart?.giftsInfo ?: listOf()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts

    fun getGifts(userId: Long, giftsIds: List<String>) = viewModelScope.launch {
        try {
            val clientGifts = giftsIds.mapNotNull { getGiftUseCase(userId, it) }
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }
}
