package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FriendsWishlistViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val getClientState: GetClientStateUseCase,
    private val giftUseCase: GiftUseCase,
    private val clientFBUseCase: ClientFBUseCase
) : ViewModel() {
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
            val client = getClientState()
            try {
                val curGift =
                    giftUseCase.getGift(friend.vkId, friend.wishlists[wishlistIndex].giftsIds[pos])
                curGift?.let { gift ->
                    gift.isChosen = isChecked
                    if (isChecked) {
                        client?.cart?.giftsInfo?.add(
                            GiftInfo(
                                gift.id,
                                gift.forId,
                                Calendar.getInstance()
                            )
                        )
                    } else {
                        val giftToDelete =
                            client?.cart?.giftsInfo?.firstOrNull { giftInfo -> giftInfo.giftId == gift.id }
                        client?.cart?.giftsInfo?.remove(giftToDelete)
                    }
                    giftUseCase.updateGift(friend.vkId, gift)
                    updateClient(client)
                    _friend.value = Result.success(curFriend)
                }
            } catch (ex: Exception) {
                _friend.value = Result.failure(ex)
            }
        }
    }

    private fun updateClient(client: Client?) = viewModelScope.launch {
        client?.let {
            clientFBUseCase.updateCart(it.vkId, client.cart.giftsInfo)
            getClientState.addClient(it)
        }
    }

    fun getClientCart(): List<GiftInfo> = getClientState()?.cart?.giftsInfo ?: listOf()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts

    fun getGifts(userId: Long, giftsIds: List<String>) = viewModelScope.launch {
        try {
            val clientGifts = giftsIds.mapNotNull { giftUseCase.getGift(userId, it) }
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }
}
