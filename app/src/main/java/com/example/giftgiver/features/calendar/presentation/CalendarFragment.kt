package com.example.giftgiver.features.calendar.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.EventDay
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.event.presentation.AddEventDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        calendarViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[CalendarViewModel::class.java]
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_calendar, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initObservers()
        calendarViewModel.getHolidays()
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
                    calendarViewModel.deleteClientsEvents()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }
                .create()
            dialog.show()
        }
    }

    private fun bindCalendar(holidays: List<Event>) = with(binding) {
        val eventDays: List<EventDay> = holidays.map {
            EventDay(                            //todo get rid of all these maps
                it.date,
                R.color.accent
            )
        }
        tvDescription.movementMethod = ScrollingMovementMethod()
        calendar.setEvents(eventDays)
        calendar.setHighlightedDays(eventDays.map { it.calendar })
        calendar.setOnDayClickListener { event ->
            tvDate.text = dateFormat.format(event.calendar.time)
            val eventsDesc = holidays.filter { it.date == event.calendar }
                .map { it.desc }
                .joinToString(",\n")
            tvDescription.text = eventsDesc
        }
    }

    private fun enterEditMode() {
        AddEventDialog().show(childFragmentManager, "dialog")
    }

    fun addEvent(event: Event) = calendarViewModel.addEvent(event)

    private fun initObservers() {
        calendarViewModel.holidays.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val holidays = it
                bindCalendar(holidays)
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
