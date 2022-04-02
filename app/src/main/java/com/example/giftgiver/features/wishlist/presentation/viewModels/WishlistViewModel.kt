package com.example.giftgiver.features.wishlist.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class WishlistViewModel @Inject constructor(
    private val updateWishlists: UpdateWishlistUseCase,
    private val imageStorage: ImageStorage,
    private val clientStateRep: ClientStateRep
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

    private fun updateClient() = viewModelScope.launch {
        client?.let { client ->
            updateWishlists(client.vkId, client.wishlists)
            client.wishlists = client.wishlists
            clientStateRep.addClient(client)
        }
    }

    fun addGift(newName: String, newDesc: String, newImageFile: File?) = client?.let { client ->
        viewModelScope.launch {
            try {
                val defaultImageUri = getDefaultUri()
                val gift = Gift(
                    newName,
                    client.vkId,
                    client.info.name,
                    newDesc,
                    defaultImageUri,
                    wishlistIndex = wishlistIndex
                )
                newImageFile?.let {
                    gift.imageUrl = imageStorage.addImage(newImageFile).toString()
                    client.wishlists[wishlistIndex].gifts.add(gift)
                    updateClient()
                    _wishlist.value = Result.success(client.wishlists[wishlistIndex])
                }
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    fun deleteGift(gift: Gift) = client?.let { client ->
        viewModelScope.launch {
            try {
                client.wishlists[wishlistIndex].gifts.remove(gift)
                updateClient()
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
                updateClient()
                _wishlist.value = Result.success(client.wishlists[wishlistIndex])
            } catch (ex: Exception) {
                _wishlist.value = Result.failure(ex)
            }
        }
    }

    private suspend fun getDefaultUri() = imageStorage.getDefaultUrl().toString()
}
