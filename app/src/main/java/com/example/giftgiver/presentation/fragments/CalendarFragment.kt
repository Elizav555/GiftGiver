package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.EventDay
import com.example.giftgiver.R
import com.example.giftgiver.data.holidaysApi.HolidayRepositoryImpl
import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.di.DIContainer
import com.example.giftgiver.domain.usecase.GetHolidays
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val client = ClientState.client
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private lateinit var getHolidaysUseCase: GetHolidays
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    //todo реализовать appbar add
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        init()
        bindCalendar()
    }

    private fun init() {
        getHolidaysUseCase = GetHolidays(
            holidayRepository = HolidayRepositoryImpl(
                api = DIContainer().api,
                holidayMapper = HolidayMapper(),
            ),
            dispatcher = Dispatchers.Default
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                addEvent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bindCalendar() = with(binding) {
        lifecycleScope.launch {
            val holidays = getHolidaysUseCase("2022")
            val eventDays = holidays.map {
                EventDay(
                    it.date,
                    R.color.accent
                )
            }
            calendarView.setEvents(eventDays)
            calendarView.setHighlightedDays(eventDays.map { it.calendar })
            calendarView.setOnDayClickListener { event ->
                tvDate.text = dateFormat.format(event.calendar.time)
                tvDescription.text = holidays.find { it.date == event.calendar }?.desc
            }
        }
    }

    private fun addEvent() {
        TODO("Not yet implemented")
    }
}
