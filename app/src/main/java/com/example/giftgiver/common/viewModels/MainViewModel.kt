package com.example.giftgiver.common.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.useCases.ClientUpdatedUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.UpdateClientStateUseCase
import com.example.giftgiver.features.client.domain.useCases.UpdateOfflineClientUseCase
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val clientUpdated: ClientUpdatedUseCase,
    private val updateClientState: UpdateClientStateUseCase,
    private val updateOfflineClient: UpdateOfflineClientUseCase,
    private val getClientByVkId: GetClientByVkId
) : ViewModel() {
    fun isClientChanged(): LiveData<Boolean> = clientUpdated.isClientChanged()

    fun onClientChanged() = viewModelScope.launch {
        getClientByVkId(VK.getUserId().value)?.let {
            updateClientState(it)
            updateOfflineClient(it)
        }
    }
}
