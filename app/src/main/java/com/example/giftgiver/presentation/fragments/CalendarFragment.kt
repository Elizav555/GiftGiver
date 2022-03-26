package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.EventDay
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.holidaysApi.HolidayRepositoryImpl
import com.example.giftgiver.data.mappers.DateMapper
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.di.DIContainer
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.usecase.GetHolidays
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val client = ClientState.client
    private val clients = ClientsRepositoryImpl(FBMapper())
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var holidays = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    //todo удалять events?
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        holidays = client?.calendar?.events ?: mutableListOf()
        if (holidays.isNullOrEmpty()) {
            getDefaultHolidays()
        } else bindCalendar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                enterEditMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDefaultHolidays() {
        lifecycleScope.launch {
            val getHolidaysUseCase = GetHolidays(
                holidayRepository = HolidayRepositoryImpl(
                    api = DIContainer().api,
                    holidayMapper = HolidayMapper(),
                ),
                dispatcher = Dispatchers.Default
            )
            val curYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            holidays = getHolidaysUseCase(curYear) as MutableList<Event>
            val dateMapper = DateMapper()
            FriendsState.friends.forEach { friend ->
                friend.bdate?.let {
                    holidays.add(
                        Event(
                            dateMapper.parseStringToCalendar(it),
                            getString(R.string.friend_bday, friend.name)
                        )
                    )
                }
            }
            bindCalendar()
            client?.let { client ->
                clients.updateCalendar(client.vkId, holidays)
                ClientState.client?.calendar?.events = holidays
            }
        }
    }

    private fun bindCalendar() = with(binding) {
        val eventDays: List<EventDay> = holidays.map {
            EventDay(
                it.date,
                R.color.accent
            )
        }
        calendar.setEvents(eventDays)
        calendar.setHighlightedDays(eventDays.map { it.calendar })
        calendar.setOnDayClickListener { event ->
            tvDate.text = dateFormat.format(event.calendar.time)
            val eventsDesc =
                holidays.filter { it.date == event.calendar }.map { it.desc }.joinToString(", ")
            tvDescription.text = eventsDesc
        }
    }

    private fun enterEditMode() {
        AddEventDialog().show(childFragmentManager, "dialog")
    }

    fun addEvent(event: Event) = client?.let { client ->
        holidays.add(event)
        clients.updateCalendar(client.vkId, holidays)
        ClientState.client?.calendar?.events = holidays
        bindCalendar()
    }
}
