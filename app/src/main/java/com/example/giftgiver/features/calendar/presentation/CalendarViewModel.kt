package com.example.giftgiver.features.calendar.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.R
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.useCases.ClientFBUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientStateUseCase
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val clientFBUseCase: ClientFBUseCase,
    private val dateMapper: DateMapper,
    private val context: Context,
    private val getClientState: GetClientStateUseCase,
    private val loadFriends: LoadFriendsUseCase
) : ViewModel() {
    private val curYear = Calendar.getInstance().get(Calendar.YEAR)
    private var client: Client? = null
    private var _holidays: MutableLiveData<Result<List<Event>>> = MutableLiveData()
    val holidays: LiveData<Result<List<Event>>> = _holidays
    private var clientHolidays: MutableList<Event> = mutableListOf()

    init {
        viewModelScope.launch {
            getClientState().collect {
                client = it
                getHolidays()
            }
        }
    }

    fun getHolidays() = viewModelScope.launch {
        try {
            clientHolidays = client?.calendar?.events ?: mutableListOf()
            if (clientHolidays.isEmpty()) {
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
}
