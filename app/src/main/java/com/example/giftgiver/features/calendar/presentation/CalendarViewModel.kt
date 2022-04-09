package com.example.giftgiver.features.calendar.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.giftgiver.R
import com.example.giftgiver.features.calendar.domain.notifications.NotifyWorker
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsUseCase
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val clientFBUseCase: ClientFBUseCase,
    private val dateMapper: DateMapper,
    private val context: Context,
    getClientState: GetClientStateUseCase,
    private val loadFriends: LoadFriendsUseCase
) : ViewModel() {

    private val curYear = Calendar.getInstance().get(Calendar.YEAR)
    private val client = getClientState()
    private var isNotified = false
    private var _holidays: MutableLiveData<Result<List<Event>>> = MutableLiveData()
    val holidays: LiveData<Result<List<Event>>> = _holidays
    private var clientHolidays: MutableList<Event> = mutableListOf()

    fun getHolidays() = viewModelScope.launch {
        try {
            clientHolidays = client?.calendar?.events ?: mutableListOf()
            if (clientHolidays.isNullOrEmpty()) {
                getDefaultHolidays()
            }
            _holidays.value = Result.success(clientHolidays)
        } catch (ex: Exception) {
            _holidays.value = Result.failure(ex)
        }
    }

    private fun updateClient() = viewModelScope.launch {
        client?.let { client ->
            clientFBUseCase.updateCalendar(client.vkId, clientHolidays)
            client.calendar.events = clientHolidays
        }
    }

    private fun mapFriendsBdays(friends: List<UserInfo>): MutableList<Event> {
        val result = mutableListOf<Event>()
        friends.forEach { friend ->
            if (!friend.bdate.isNullOrBlank()) {
                friend.bdate?.let {
                    val calendar = dateMapper.parseStringToCalendar(it)
                    calendar.set(Calendar.YEAR, curYear)
                    result.add(
                        Event(
                            calendar,
                            context.getString(R.string.friend_bday, friend.name)
                        )
                    )
                }
            }
        }
        return result
    }

    private fun getDefaultHolidays() = viewModelScope.launch {
        val friends = client?.let { loadFriends(it.vkId, false) }
        clientHolidays = getHolidaysUseCase(curYear.toString()) as MutableList<Event>
        friends?.let { mapFriendsBdays(it) }?.let { clientHolidays.addAll(it) }
        updateClient()
        _holidays.value = Result.success(clientHolidays)
    }

    fun addEvent(event: Event) =
        try {
            clientHolidays.add(event)
            updateClient()
            _holidays.value = Result.success(clientHolidays)
        } catch (ex: Exception) {
            _holidays.value = Result.failure(ex)
        }

    fun deleteClientsEvents() = viewModelScope.launch {
        try {
            clientHolidays.clear()
            getDefaultHolidays()
            _holidays.value = Result.success(clientHolidays)
        } catch (ex: Exception) {
            _holidays.value = Result.failure(ex)
        }
    }

    fun checkTomorrowEvents() {
        val tomorrow = Calendar.getInstance()
        tomorrow.add(Calendar.DAY_OF_MONTH, 1)
        val tomorrowEvents =
            clientHolidays.filter {
                dateMapper.parseCalendarToString(it.date) == dateMapper.parseCalendarToString(
                    tomorrow
                )
            }
        if (tomorrowEvents.isNotEmpty() && !isNotified) {
            val desc = tomorrowEvents.mapNotNull { it.desc }.joinToString(",\n")
            scheduleNotification(desc)
        }
    }

    private fun scheduleNotification(eventsDesc: String) {
        val data = Data.Builder().putString(NotifyWorker.EVENT_NAME, eventsDesc).build()
        val notificationWork = OneTimeWorkRequestBuilder<NotifyWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS).setInputData(data).build()
        WorkManager.getInstance(context).enqueue(notificationWork)
        isNotified = true
    }
}
