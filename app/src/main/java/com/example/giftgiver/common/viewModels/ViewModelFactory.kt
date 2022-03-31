package com.example.giftgiver.common.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.calendar.domain.useCases.UpdateCalendarUseCase
import com.example.giftgiver.features.calendar.presentation.CalendarViewModel
import com.example.giftgiver.features.event.data.DateMapper
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val updateCalendar: UpdateCalendarUseCase,
    private val dateMapper: DateMapper,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) ->
                ImageViewModel() as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(CalendarViewModel::class.java) ->
                CalendarViewModel(getHolidaysUseCase, updateCalendar, dateMapper) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
