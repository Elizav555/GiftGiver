package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.entities.EventFB
import com.example.giftgiver.data.holidaysApi.HolidayRepositoryImpl
import com.example.giftgiver.data.mappers.HolidayMapper
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.di.DIContainer
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.usecase.GetHolidays
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val client = ClientState.client

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    //todo реализовать appbar add
    //todo реализовать календарь
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val getHolidaysUseCase = GetHolidays(
            holidayRepository = HolidayRepositoryImpl(
                api = DIContainer().api,
                holidayMapper = HolidayMapper(),
            ),
            dispatcher = Dispatchers.Default
        )
        var holidays: List<Event>
        lifecycleScope.launch {
            holidays = getHolidaysUseCase("2022")
        }
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

    private fun addEvent() {
        TODO("Not yet implemented")
    }
}
