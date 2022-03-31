package com.example.giftgiver.features.calendar.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.calendar.domain.useCases.UpdateCalendarUseCase
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val updateCalendar: UpdateCalendarUseCase,
    private val dateMapper: DateMapper
) : ViewModel() {
    private val curYear = Calendar.getInstance().get(Calendar.YEAR)
    private val client = ClientState.client
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
            updateCalendar(client.vkId, clientHolidays)
            ClientState.client?.calendar?.events = clientHolidays
        }
    }

    private fun mapFriendsBdays(): MutableList<Event> {
        val result = mutableListOf<Event>()
        FriendsState.friends.forEach { friend ->
            if (!friend.bdate.isNullOrBlank()) {
                friend.bdate?.let {
                    val calendar = dateMapper.parseStringToCalendar(it)
                    calendar.set(Calendar.YEAR, curYear)
                    result.add(
                        Event(
                            calendar,
                            App.appComponent.getContext()
                                .getString(R.string.friend_bday, friend.name)
                        )
                    )
                }
            }
        }
        return result
    }

    private fun getDefaultHolidays() = viewModelScope.launch {
        clientHolidays = getHolidaysUseCase(curYear.toString()) as MutableList<Event>
        clientHolidays.addAll(mapFriendsBdays())
        updateClient()
    }

    fun addEvent(event: Event) = client?.let { client ->
        try {
            clientHolidays.add(event)
            viewModelScope.launch { updateCalendar(client.vkId, clientHolidays) }
            ClientState.client?.calendar?.events = clientHolidays
            _holidays.value = Result.success(clientHolidays)
        } catch (ex: Exception) {
            _holidays.value = Result.failure(ex)
        }
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
