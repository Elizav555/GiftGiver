package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class WishlistViewModel @Inject constructor(
    private val imageStorage: ImageStorage,
    private val getClientState: GetClientStateUseCase,
    private val giftUseCase: GiftUseCase,
    private val clientFBUseCase: ClientFBUseCase
) : ViewModel() {
    private var wishlistIndex = 0
    private var _wishlist: MutableLiveData<Result<Wishlist?>> = MutableLiveData()
    val wishlist: LiveData<Result<Wishlist?>> = _wishlist

    private var client: Client? = null

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
            }
        }
    }

    fun getWishlist(index: Int) = viewModelScope.launch {
        try {
            wishlistIndex = index
            client?.let {
                _wishlist.value = Result.success(it.wishlists[wishlistIndex])
            }
        } catch (ex: Exception) {
            _wishlist.value = Result.failure(ex)
        }
    }

    private var _gifts: MutableLiveData<Result<List<Gift>>> = MutableLiveData()
    val gifts: LiveData<Result<List<Gift>>> = _gifts
    private var clientGifts = mutableListOf<Gift>()
    fun getGifts(giftsIds: List<String>) = viewModelScope.launch {
        try {
            clientGifts =
                giftsIds.mapNotNull {
                    client?.vkId?.let { vkId ->
                        giftUseCase.getGift(
                            vkId,
                            it
                        )
                    }
                } as MutableList<Gift>
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }

    fun addGift(newName: String, newDesc: String, newImageFile: File?) =
        client?.let { client ->
            viewModelScope.launch {
                try {
                    val defaultImageUri = getDefaultUri()
                    val gift = Gift(
                        "0",
                        newName,
                        client.vkId,
                        client.info.name,
                        newDesc,
                        defaultImageUri,
                        wishlistIndex = wishlistIndex,
                        lastChanged = Calendar.getInstance()
                    )
                    newImageFile?.let {
                        gift.imageUrl = imageStorage.addImage(newImageFile).toString()
                    }
                    giftUseCase.addGift(client.vkId, gift, client.wishlists)
                    clientGifts.add(gift)
                    clientFBUseCase.updateWishlists(client.vkId, client.wishlists)
                    _wishlist.value = Result.success(client.wishlists[wishlistIndex])
                } catch (ex: Exception) {
                    _wishlist.value = Result.failure(ex)
                }
            }
        }

    fun deleteGift(gift: Gift) = client?.let { client ->
        viewModelScope.launch {
            try {
                clientGifts.remove(gift)
                giftUseCase.deleteGift(client.vkId, gift, client.wishlists)
                client.wishlists[wishlistIndex].giftsIds.remove(gift.id)
                clientFBUseCase.updateWishlists(client.vkId, client.wishlists)
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    fun moveGift(fromPos: Int, toPos: Int) = client?.let { client ->
        viewModelScope.launch {
            try {
                Collections.swap(clientGifts, fromPos, toPos)
                Collections.swap(client.wishlists[wishlistIndex].giftsIds, fromPos, toPos)
                clientFBUseCase.updateWishlists(client.vkId, client.wishlists)
                //               getClientState.addClient(client)
                _gifts.value = Result.success(clientGifts)
            } catch (ex: Exception) {
                _gifts.value = Result.failure(ex)
            }
        }
    }

    fun changeWishlistName(newName: String) = client?.let { client ->
        viewModelScope.launch {
            try {
                client.wishlists[wishlistIndex].name = newName
                clientFBUseCase.updateWishlists(client.vkId, client.wishlists)
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    fun getClientId() = client?.vkId

    private suspend fun getDefaultUri() = imageStorage.getDefaultUrl().toString()
    fun getGiftByPos(pos: Int) = _gifts.value?.getOrNull()?.get(pos)
}
