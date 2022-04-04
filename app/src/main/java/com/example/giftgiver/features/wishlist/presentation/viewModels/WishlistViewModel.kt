package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.useCases.AddGiftUseCase
import com.example.giftgiver.features.gift.domain.useCases.DeleteGiftUseCase
import com.example.giftgiver.features.gift.domain.useCases.GetGiftUseCase
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class WishlistViewModel @Inject constructor(
    private val imageStorage: ImageStorage,
    private val clientStateRep: ClientStateRep,
    private val getGiftUseCase: GetGiftUseCase,
    private val addGiftUseCase: AddGiftUseCase,
    private val deleteGiftUseCase: DeleteGiftUseCase,
    private val updateWishlist: UpdateWishlistUseCase,
) : ViewModel() {
    private val client = clientStateRep.getClient()
    private var wishlistIndex = 0
    private var _wishlist: MutableLiveData<Result<Wishlist?>> = MutableLiveData()
    val wishlist: LiveData<Result<Wishlist?>> = _wishlist

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

    fun getGifts(giftsIds: List<String>) = viewModelScope.launch {
        try {
            val clientGifts =
                giftsIds.mapNotNull { client?.vkId?.let { vkId -> getGiftUseCase(vkId, it) } }
            _gifts.value = Result.success(clientGifts)
        } catch (ex: Exception) {
            _gifts.value = Result.failure(ex)
        }
    }

    fun addGift(newName: String, newDesc: String, newImageFile: File?) = client?.let { client ->
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
                addGiftUseCase(client.vkId, gift, client.wishlists)
                updateWishlist(client.vkId, client.wishlists)
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    fun deleteGift(gift: Gift) = client?.let { client ->
        viewModelScope.launch {
            try {
                deleteGiftUseCase(client.vkId, gift, client.wishlists)
                updateWishlist(client.vkId, client.wishlists)
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    fun changeWishlistName(newName: String) = client?.let { client ->
        viewModelScope.launch {
            try {
                client.wishlists[wishlistIndex].name = newName
                updateWishlist(client.vkId, client.wishlists)
                clientStateRep.addClient(client)
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    private suspend fun getDefaultUri() = imageStorage.getDefaultUrl().toString()
}
