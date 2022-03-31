package com.example.giftgiver.common.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.calendar.domain.useCases.UpdateCalendarUseCase
import com.example.giftgiver.features.calendar.presentation.CalendarViewModel
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.cart.presentation.CartViewModel
import com.example.giftgiver.features.client.domain.useCases.UpdateClientsInfoUseCase
import com.example.giftgiver.features.client.presentation.ClientViewModel
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val updateCalendar: UpdateCalendarUseCase,
    private val dateMapper: DateMapper,

    private val updateCart: UpdateCartUseCase,

    private val updateWishlists: UpdateWishlistUseCase,
    private val updateInfo: UpdateClientsInfoUseCase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) ->
                ImageViewModel() as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(CalendarViewModel::class.java) ->
                CalendarViewModel(getHolidaysUseCase, updateCalendar, dateMapper) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(updateCart) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            modelClass.isAssignableFrom(ClientViewModel::class.java) ->
                ClientViewModel(updateWishlists, updateInfo) as? T
                    ?: throw IllegalArgumentException("Unknown ViewModel class")
            else ->
                throw IllegalArgumentException("Unknown ViewModel class")
        }
}
