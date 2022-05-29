package com.example.giftgiver.features.calendar.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.applandeo.materialcalendarview.EventDay
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.features.event.domain.Event
import com.example.giftgiver.features.event.presentation.AddEventDialog
import com.example.giftgiver.utils.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CalendarFragment : BaseFragment(R.layout.fragment_calendar) {
    private lateinit var binding: FragmentCalendarBinding
    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val calendarViewModel: CalendarViewModel by viewModel()

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBarChangesListener.onToolbarChanges(
            AppBarConfig(
                firstButton = AppBarButton(R.drawable.ic_baseline_add_24, ::enterEditMode),
                secondButton = AppBarButton(
                    R.drawable.ic_baseline_delete_outline_24,
                    ::showDeleteDialog
                ),
                title = "Calendar"
            )
        )
        initObservers()
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
        tvDescription.movementMethod = ScrollingMovementMethod()
        calendar.setOnDayClickListener { event ->
            tvDate.text = dateFormat.format(event.calendar.time)
            displayEventsDesc(holidays.filter { it.date == event.calendar })
        }
        bindEvents(holidays)
        val currentDate = calendar.selectedDates.first()
        val currentEvents = holidays.filter { it.date == currentDate }
        displayEventsDesc(currentEvents)
        tvDate.text = dateFormat.format(currentDate.time)
    }

    private fun bindEvents(holidays: List<Event>) = with(binding) {
        val eventDays: List<EventDay> = holidays.map {
            EventDay(
                it.date,
                R.drawable.ic_baseline_celebration_24,
                ResourcesCompat.getColor(resources, R.color.accent, null)
            )
        }
        calendar.setEvents(eventDays)
    }

    private fun displayEventsDesc(events: List<Event>) = with(binding) {
        val eventsDesc = events.mapNotNull { it.desc }
            .joinToString(",\n")
        tvDescription.text = eventsDesc
        tvDescription.scrollTo(0, 0)
    }

    private fun enterEditMode() {
        AddEventDialog().show(childFragmentManager, "dialog")
    }

    fun addEvent(event: Event) = calendarViewModel.addEvent(event)

    private fun initObservers() {
        calendarViewModel.holidays.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                bindCalendar(it)
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
