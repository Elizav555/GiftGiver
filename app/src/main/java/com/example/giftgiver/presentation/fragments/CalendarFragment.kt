package com.example.giftgiver.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.applandeo.materialcalendarview.EventDay
import com.example.giftgiver.R
import com.example.giftgiver.data.mappers.DateMapper
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.domain.usecase.GetHolidaysUseCase
import com.example.giftgiver.domain.usecase.UpdateCalendarUseCase
import com.example.giftgiver.presentation.App
import com.example.giftgiver.presentation.dialogs.AddEventDialog
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val client = ClientState.client

    @Inject
    lateinit var getHolidaysUseCase: GetHolidaysUseCase

    @Inject
    lateinit var updateCalendar: UpdateCalendarUseCase
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var holidays = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_calendar, menu)
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
            R.id.delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.deleteEventsTitle))
                .setMessage(getString(R.string.deleteEventsMessage))
                .setPositiveButton(R.string.delete_all) { _, _ ->
                    deleteClientsEvents()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }
                .create()
            dialog.show()
        }
    }

    private fun getDefaultHolidays() {
        lifecycleScope.launch {
            val curYear = Calendar.getInstance().get(Calendar.YEAR)
            holidays = getHolidaysUseCase(curYear.toString()) as MutableList<Event>
            val dateMapper = DateMapper()
            FriendsState.friends.forEach { friend ->
                if (!friend.bdate.isNullOrBlank()) {
                    friend.bdate?.let {
                        val calendar = dateMapper.parseStringToCalendar(it)
                        calendar.set(Calendar.YEAR, curYear)
                        holidays.add(
                            Event(
                                calendar,
                                getString(R.string.friend_bday, friend.name)
                            )
                        )
                    }
                }
            }
            bindCalendar()
            client?.let { client ->
                updateCalendar(client.vkId, holidays)
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
        tvDescription.movementMethod = ScrollingMovementMethod()
        calendar.setEvents(eventDays)
        calendar.setHighlightedDays(eventDays.map { it.calendar })
        calendar.setOnDayClickListener { event ->
            tvDate.text = dateFormat.format(event.calendar.time)
            val eventsDesc =
                holidays.filter { it.date == event.calendar }.map { it.desc }.joinToString(",\n")
            tvDescription.text = eventsDesc
        }
    }

    private fun enterEditMode() {
        AddEventDialog().show(childFragmentManager, "dialog")
    }

    fun addEvent(event: Event) = client?.let { client ->
        holidays.add(event)
        lifecycleScope.launch { updateCalendar(client.vkId, holidays) }
        ClientState.client?.calendar?.events = holidays
        bindCalendar()
    }

    private fun deleteClientsEvents() {
        holidays.clear()
        getDefaultHolidays()
    }
}
