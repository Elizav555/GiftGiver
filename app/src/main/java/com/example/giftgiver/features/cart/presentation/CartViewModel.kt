package com.example.giftgiver.features.cart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo
import com.example.giftgiver.features.gift.domain.useCases.GetGiftUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val updateCart: UpdateCartUseCase,
    private val clientStateRep: ClientStateRep,
    private val getGiftUseCase: GetGiftUseCase
) : ViewModel() {
    private val client = clientStateRep.getClient()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts
    private var clientGifts: MutableList<Gift> = mutableListOf()

    fun getGifts() = viewModelScope.launch {
        try {
            val giftsMapped = client?.cart?.giftsInfo?.mapNotNull {
                getGiftUseCase(
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

    private fun updateClient() = viewModelScope.launch {
        client?.let { client ->
            val ids = clientGifts.map { GiftInfo(it.id, it.forId, Calendar.getInstance()) }
            updateCart(client.vkId, ids)
            client.cart.giftsInfo = ids as MutableList<GiftInfo>
            clientStateRep.addClient(client)
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
