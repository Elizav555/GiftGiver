package com.example.giftgiver.features.gift.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.useCases.GetGiftUseCase
import com.example.giftgiver.features.gift.domain.useCases.UpdateGiftUseCase
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class GiftViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val imageStorage: ImageStorage,
    private val clientStateRep: ClientStateRep,
    private val getGiftUseCase: GetGiftUseCase,
    private val updateGiftUseCase: UpdateGiftUseCase
) : ViewModel() {
    private val client = clientStateRep.getClient()

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
            clientGift = getGiftUseCase(userId, giftId)
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
                    client?.vkId?.let { vkId -> updateGiftUseCase(vkId, it) }
                    _gift.value = Result.success(it)
                }
            } catch (ex: Exception) {
                _gift.value = Result.failure(ex)
            }
        }
}
