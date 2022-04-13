package com.example.giftgiver.common.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.features.client.domain.useCases.ClientsChangesUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.client.domain.useCases.UpdateOfflineClientUseCase
import com.example.giftgiver.features.start.domain.AuthUseCase
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val clientsChanges: ClientsChangesUseCase,
    private val getClientState: GetClientStateUseCase,
    private val updateOfflineClient: UpdateOfflineClientUseCase,
    private val getClientByVkId: GetClientByVkId,
    private val authUseCase: AuthUseCase
) : ViewModel() {
    fun isClientChanged(): LiveData<Boolean> = clientsChanges.isClientChanged()

    fun onClientChanged() = viewModelScope.launch {
        getClientByVkId(VK.getUserId().value)?.let {
            getClientState.addClient(it)
            updateOfflineClient(it)
        }
    }

    fun restart() = authUseCase.restart()

    fun isClientAuth() = clientsChanges.isClientAuth()

    fun hasInternetConnection(): LiveData<Boolean> = clientsChanges.hasInternetConnection()
}
