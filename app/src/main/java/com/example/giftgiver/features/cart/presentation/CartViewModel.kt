package com.example.giftgiver.features.cart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val clientFBUseCase: ClientFBUseCase,
    getClientState: GetClientStateUseCase,
    private val giftUseCase: GiftUseCase
) : ViewModel() {
    private val client = getClientState()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts
    private var clientGifts: MutableList<Gift> = mutableListOf()

    fun getGifts() = viewModelScope.launch {
        try {
            val giftsMapped = client?.cart?.giftsInfo?.mapNotNull {
                giftUseCase.getGift(
                    it.forId,
                    it.giftId
                )
            }
            clientGifts = (giftsMapped ?: listOf()) as MutableList<Gift>
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }

    fun moveGift(fromPos: Int, toPos: Int) =
        try {
            Collections.swap(clientGifts, fromPos, toPos)
            updateClient()
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }


    fun updateClient() = viewModelScope.launch {
        client?.let { client ->
            val ids = clientGifts.map { GiftInfo(it.id, it.forId, Calendar.getInstance()) }
            clientFBUseCase.updateCart(client.vkId, ids)
            client.cart.giftsInfo = ids as MutableList<GiftInfo>
        }
    }

    fun deleteAll() = try {
        client?.let {
            clientGifts.clear()
            updateClient()
            _gifts.value = Result.success(clientGifts)
        }
    } catch (ex: Exception) {
        _gifts.value = Result.failure(ex)
    }

    fun delete(gift: Gift) = try {
        client?.let {
            clientGifts.remove(gift)
            updateClient()
            _gifts.value = Result.success(clientGifts)
        }
    } catch (ex: Exception) {
        _gifts.value = Result.failure(ex)
    }
}
