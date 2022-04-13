package com.example.giftgiver.features.client.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import com.example.giftgiver.features.start.domain.AuthUseCase
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ClientViewModel @Inject constructor(
    private val imageStorage: ImageStorage,
    private val getClientState: GetClientStateUseCase,
    private val clientFBUseCase: ClientFBUseCase,
    private val authUseCase: AuthUseCase,
    private val giftUseCase: GiftUseCase
) : ViewModel() {
    private val client = getClientState()
    private var _wishlists: MutableLiveData<Result<List<Wishlist>>> = MutableLiveData()
    val wishlists: LiveData<Result<List<Wishlist>>> = _wishlists
    private var clientWishlists: MutableList<Wishlist> = mutableListOf()

    fun getClient() = client
    fun getWishlists() =
        try {
            clientWishlists = client?.wishlists ?: mutableListOf()
            _wishlists.value = Result.success(clientWishlists)
        } catch (ex: Exception) {
            _wishlists.value = Result.failure(ex)
        }

    private fun updateClientsWishlists() = viewModelScope.launch {
        client?.let { client ->
            clientFBUseCase.updateWishlists(client.vkId, clientWishlists)
            client.wishlists = clientWishlists
            getClientState.addClient(client)
        }
    }

    fun addWishlist(wishlist: Wishlist) = try {
        clientWishlists.add(wishlist)
        updateClientsWishlists()
        _wishlists.value = Result.success(clientWishlists)
    } catch (ex: Exception) {
        _wishlists.value = Result.failure(ex)
    }

    fun deleteWishlist(wishlist: Wishlist) = viewModelScope.launch {
        try {
            client?.let {
                wishlist.giftsIds.forEach { giftId ->
                    giftUseCase.getGift(client.vkId, giftId)?.let { gift ->
                        giftUseCase.deleteGift(
                            client.vkId,
                            gift,
                            client.wishlists
                        )
                    }
                }
            }
            clientWishlists.remove(wishlist)
            updateClientsWishlists()
            _wishlists.value = Result.success(clientWishlists)
        } catch (ex: Exception) {
            _wishlists.value = Result.failure(ex)
        }
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
                clientFBUseCase.updateInfo(client.vkId, it)
                client.info = it
                getClientState.addClient(client)
            }
        }
    }

    fun updateInfo(newName: String, newInfo: String, newBirthDate: String, imageFile: File?) =
        viewModelScope.launch {
            try {
                imageFile?.let { file ->
                    clientInfo?.photo = imageStorage.addImage(file).toString()
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

    fun logout() = authUseCase.logout()
}
