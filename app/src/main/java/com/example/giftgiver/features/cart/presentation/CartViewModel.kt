package com.example.giftgiver.features.cart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val updateCart: UpdateCartUseCase,
    private val clientStateRep: ClientStateRep,
) : ViewModel() {
    private val client = clientStateRep.getClient()

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts
    private var clientGifts: MutableList<Gift> = mutableListOf()

    fun getGifts() = viewModelScope.launch {
        try {
            clientGifts = client?.cart?.gifts ?: mutableListOf()
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }

    private fun updateClient() = viewModelScope.launch {
        client?.let { client ->
            updateCart(client.vkId, clientGifts)
            client.cart.gifts = clientGifts
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
