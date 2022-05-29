package com.example.giftgiver.features.start.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.giftgiver.features.calendar.domain.notifications.NotifyWorker
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.AddClientUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.client.domain.useCases.UpdateTokenUseCase
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.start.domain.AuthUseCase
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsUseCase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val getClientByVkId: GetClientByVkId,
    private val loadFriends: LoadFriendsUseCase,
    private val addClientUseCase: AddClientUseCase,
    private val getClientState: GetClientStateUseCase,
    private val authUseCase: AuthUseCase,
    private val clientStateUseCase: GetClientStateUseCase,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val dateMapper: DateMapper,
    private val context: Context,
) : ViewModel() {

    private var _client: MutableLiveData<Result<Client?>> = MutableLiveData()
    val client: LiveData<Result<Client?>> = _client

    private var _friends: MutableLiveData<Result<List<UserInfo>>> = MutableLiveData()
    val friends: LiveData<Result<List<UserInfo>>> = _friends

    fun getClient(vkId: Long) = viewModelScope.launch {
        try {
            val clientReceived = getClientByVkId(vkId)
            clientReceived?.let {
                checkTokenUpdates(it.vkId)
                checkTomorrowEvents(it)
            }
            _client.value = Result.success(clientReceived)
        } catch (ex: Exception) {
            _client.value = Result.failure(ex)
        }
    }

    fun addClient(clientToAdd: Client) = viewModelScope.launch {
        addClientUseCase(clientToAdd)
    }

    fun addClientState(clientToAdd: Client) = viewModelScope.launch {
        clientStateUseCase.addClient(clientToAdd)
    }

    fun loadFriends() = viewModelScope.launch {
        try {
            val friendsReceived = loadFriends(VK.getUserId().value)
            getClientState.addFriends(friendsReceived)
            _friends.value = Result.success(friendsReceived)
        } catch (ex: Exception) {
            _friends.value = Result.failure(ex)
        }
    }

    fun login() = authUseCase.login()


    private fun checkTokenUpdates(vkId: Long) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            viewModelScope.launch {
                task.result?.let {
                    updateTokenUseCase(vkId, it)
                }
            }
        })
    }

    private fun checkTomorrowEvents(client: Client) {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowEvents =
            client.calendar.events.filter {
                dateMapper.parseCalendarToString(it.date) == dateMapper.parseCalendarToString(
                    tomorrow
                )
            }
        if (tomorrowEvents.isNotEmpty()) {
            val desc = tomorrowEvents.mapNotNull { it.desc }.joinToString(",\n")
            scheduleNotification(desc)
        }
    }

    private fun scheduleNotification(eventsDesc: String) {
        val data = Data.Builder().putString(NotifyWorker.EVENT_NAME, eventsDesc).build()
        val notificationWork = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInitialDelay(20, TimeUnit.SECONDS).setInputData(data).build()
        WorkManager.getInstance(context).enqueue(notificationWork)
    }
}
