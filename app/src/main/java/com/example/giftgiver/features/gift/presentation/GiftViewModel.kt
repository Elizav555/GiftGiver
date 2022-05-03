package com.example.giftgiver.features.gift.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import com.example.giftgiver.features.images.domain.ImageStorage
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class GiftViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val imageStorage: ImageStorage,
    getClientState: GetClientStateUseCase,
    private val giftUseCase: GiftUseCase,
) : ViewModel() {
    private var client: Client? = null

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
            }
        }
    }

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

    fun getGift(userId: Long, giftId: String) = viewModelScope.launch {
        try {
            clientGift = giftUseCase.getGift(userId, giftId)
            _gift.value = Result.success(clientGift)
        } catch (ex: Exception) {
            _gift.value = Result.failure(ex)
        }
    }

    fun changeGift(newName: String, newDesc: String, newImageFile: File?) =
        viewModelScope.launch {
            try {
                clientGift?.let {
                    newImageFile?.let { file ->
                        it.imageUrl = imageStorage.addImage(file).toString()
                    }
                    it.name = newName
                    it.desc = newDesc
                    it.lastChanged = Calendar.getInstance()
                    client?.vkId?.let { vkId -> giftUseCase.updateGift(vkId, it) }
                    _gift.value = Result.success(it)
                }
            } catch (ex: Exception) {
                _gift.value = Result.failure(ex)
            }
        }
}
