package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBInteractor
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.gift.domain.useCases.GiftInteractor
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FriendsWishlistViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val getClientState: GetClientStateUseCase,
    private val giftInteractor: GiftInteractor,
    private val clientFBInteractor: ClientFBInteractor
) : ViewModel() {
    private var _friend: MutableLiveData<Result<Client?>> = MutableLiveData()
    val friend: LiveData<Result<Client?>> = _friend
    private var curFriend: Client? = null

    private var client: Client? = null

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
            }
        }
    }

    fun getFriend(vkId: Long) = viewModelScope.launch {
        try {
            curFriend = getClientByVkId(vkId)
            _friend.value = Result.success(curFriend)
        } catch (ex: Exception) {
            _friend.value = Result.failure(ex)
        }
    }

    fun checkedFunc(giftId: String, isChecked: Boolean) =
        curFriend?.let { friend ->
            viewModelScope.launch {
                try {
                    val curGift =
                        giftInteractor.getGift(friend.vkId, giftId)
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
                        launch { giftInteractor.updateGift(friend.vkId, gift) }
                        launch { updateClient() }
                        _friend.value = Result.success(curFriend)
                    }
                } catch (ex: Exception) {
                    _friend.value = Result.failure(ex)
                }
            }
        }

    private suspend fun updateClient() = client?.let {
        clientFBInteractor.updateCart(it.vkId, it.cart.giftsInfo)
    }

    fun getClientCart(): List<GiftInfo> = client?.cart?.giftsInfo ?: listOf()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts

    fun getGifts(userId: Long, giftsIds: List<String>) = viewModelScope.launch {
        try {
            val clientGifts = giftsIds.mapNotNull { giftInteractor.getGift(userId, it) }
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }
}
