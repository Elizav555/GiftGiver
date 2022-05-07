package com.example.giftgiver.features.client.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import com.example.giftgiver.features.images.domain.AddImageUseCase
import com.example.giftgiver.features.images.domain.DeleteImageUseCase
import com.example.giftgiver.features.start.domain.AuthUseCase
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ClientViewModel @Inject constructor(
    private val addImageUseCase: AddImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val getClientState: GetClientStateUseCase,
    private val clientFBUseCase: ClientFBUseCase,
    private val authUseCase: AuthUseCase,
    private val giftUseCase: GiftUseCase
) : ViewModel() {
    private var _wishlists: MutableLiveData<Result<List<Wishlist>>> = MutableLiveData()
    val wishlists: LiveData<Result<List<Wishlist>>> = _wishlists
    private var clientWishlists: MutableList<Wishlist> = mutableListOf()

    private var client: Client? = null

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
            }
        }
    }

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
            client?.let { client ->
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
            }
        }
    }

    fun updateInfo(newName: String, newInfo: String, newBirthDate: String, imageFile: File?) =
        viewModelScope.launch {
            try {
                imageFile?.let { file ->
                    var oldPhoto = clientInfo?.photo
                    clientInfo?.photo = addImageUseCase(file).toString()
                    oldPhoto?.let { deleteImageUseCase(it) }
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
