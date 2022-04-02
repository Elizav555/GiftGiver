package com.example.giftgiver.features.gift.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class GiftViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val updateWishlists: UpdateWishlistUseCase,
    private val imageStorage: ImageStorage,
    private val clientStateRep: ClientStateRep,
) : ViewModel() {
    private val client = clientStateRep.getClient()
    private var wishlistIndex = 0
    private var giftIndex = 0

    private var _gift: MutableLiveData<Result<Gift?>> = MutableLiveData()
    val gift: LiveData<Result<Gift?>> = _gift
    private var clientGift: Gift? = null

    private var _isClient: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isClient: LiveData<Result<Boolean>> = _isClient

    fun checkUser(vkId: Long) = viewModelScope.launch {
        try {
            val clientReceivedId = getClientByVkId(vkId)?.vkId
            _isClient.value = Result.success(clientReceivedId == client?.vkId)
        } catch (ex: Exception) {
            _isClient.value = Result.failure(ex)
        }
    }

    fun getGift(userId: Long, wIndex: Int, gIndex: Int) = viewModelScope.launch {
        try {
            val clientReceived = getClientByVkId(userId)
            wishlistIndex = wIndex
            giftIndex = gIndex
            clientGift = clientReceived?.wishlists?.get(wishlistIndex)?.gifts?.get(giftIndex)
            _gift.value = Result.success(clientGift)
        } catch (ex: Exception) {
            _gift.value = Result.failure(ex)
        }
    }

    private fun updateClientsWishlists() = viewModelScope.launch {
        client?.let { client ->
            clientGift?.let {
                client.wishlists[wishlistIndex].gifts[giftIndex] = it
                updateWishlists(client.vkId, client.wishlists)
                client.wishlists = client.wishlists
                clientStateRep.addClient(client)
            }
        }
    }

    fun changeGift(newName: String, newDesc: String, newImageFile: File?) = client?.let {
        viewModelScope.launch {
            try {
                val gift = client.wishlists[wishlistIndex].gifts[giftIndex]
                newImageFile?.let {
                    gift.imageUrl = imageStorage.addImage(newImageFile).toString()
                }
                clientGift =
                    Gift(
                        newName,
                        gift.forId,
                        gift.forName,
                        newDesc,
                        gift.imageUrl,
                        gift.isChosen,
                        wishlistIndex
                    )
                updateClientsWishlists()
                _gift.value = Result.success(clientGift)
            } catch (ex: Exception) {
                _gift.value = Result.failure(ex)
            }
        }
    }
}
