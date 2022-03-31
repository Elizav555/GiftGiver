package com.example.giftgiver.features.client.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorageImpl
import com.example.giftgiver.features.client.domain.useCases.UpdateClientsInfoUseCase
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ClientViewModel @Inject constructor(
    private val updateWishlists: UpdateWishlistUseCase,
    private val updateInfo: UpdateClientsInfoUseCase,
) : ViewModel() {
    private val client = ClientState.client
    private var _wishlists: MutableLiveData<Result<List<Wishlist>>> = MutableLiveData()
    val wishlists: LiveData<Result<List<Wishlist>>> = _wishlists
    private var clientWishlists: MutableList<Wishlist> = mutableListOf()

    fun getWishlists() = viewModelScope.launch {
        try {
            clientWishlists = client?.wishlists ?: mutableListOf()
            _wishlists.value = Result.success(clientWishlists)
        } catch (ex: Exception) {
            _wishlists.value = Result.failure(ex)
        }
    }

    private fun updateClientsWishlists() = viewModelScope.launch {
        client?.let { client ->
            updateWishlists(client.vkId, clientWishlists)
            ClientState.client?.wishlists = clientWishlists
        }
    }

    fun addWishlist(wishlist: Wishlist) = try {
        clientWishlists.add(wishlist)
        updateClientsWishlists()
        _wishlists.value = Result.success(clientWishlists)
    } catch (ex: Exception) {
        _wishlists.value = Result.failure(ex)
    }

    fun deleteWishlist(wishlist: Wishlist) = try {
        clientWishlists.remove(wishlist)
        updateClientsWishlists()
        _wishlists.value = Result.success(clientWishlists)
    } catch (ex: Exception) {
        _wishlists.value = Result.failure(ex)
    }

    private var _info: MutableLiveData<Result<UserInfo?>> = MutableLiveData()
    val info: LiveData<Result<UserInfo?>> = _info
    private var clientInfo: UserInfo? = null

    fun getInfo() = viewModelScope.launch {
        try {
            clientInfo = client?.info
            _info.value = Result.success(clientInfo)
        } catch (ex: Exception) {
            _info.value = Result.failure(ex)
        }
    }

    private fun updateClientsInfo() = viewModelScope.launch {
        client?.let { client ->
            clientInfo?.let {
                updateInfo(client.vkId, it)
                ClientState.client?.info = it
            }
        }
    }

    fun updateInfo(newName: String, newInfo: String, newBirthDate: String, imageFile: File?) =
        viewModelScope.launch {
            try {
                imageFile?.let { file ->
                    clientInfo?.photo = ImageStorageImpl().addImage(file).toString()
                }
                clientInfo?.name = newName
                clientInfo?.about = newInfo
                clientInfo?.bdate = newBirthDate
                updateClientsInfo()
                _info.value = Result.success(clientInfo)
            } catch (ex: Exception) {
                _info.value = Result.failure(ex)
            }
        }
}