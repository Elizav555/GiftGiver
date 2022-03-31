package com.example.giftgiver.common.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.calendar.domain.useCases.UpdateCalendarUseCase
import com.example.giftgiver.features.calendar.presentation.CalendarViewModel
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.cart.presentation.CartViewModel
import com.example.giftgiver.features.client.domain.useCases.AddClientUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.client.domain.useCases.UpdateClientsInfoUseCase
import com.example.giftgiver.features.client.presentation.ClientViewModel
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.gift.presentation.GiftViewModel
import com.example.giftgiver.features.start.presentation.StartViewModel
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsVK
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getHolidaysUseCase: GetHolidaysUseCase,
    private val updateCalendar: UpdateCalendarUseCase,
    private val dateMapper: DateMapper,
    private val updateCart: UpdateCartUseCase,
    private val updateWishlists: UpdateWishlistUseCase,
    private val updateInfo: UpdateClientsInfoUseCase,
    private val imageStorage: ImageStorage,
    private val getClientByVkId: GetClientByVkId,
    private val loadFriendsVK: LoadFriendsVK,
    private val addClient: AddClientUseCase,
) : ViewModelProvider.Factory {
    val error = App.appComponent.getContext().getString(R.string.unknownViewModel)
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) ->
                ImageViewModel() as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(CalendarViewModel::class.java) ->
                CalendarViewModel(getHolidaysUseCase, updateCalendar, dateMapper) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(updateCart) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(ClientViewModel::class.java) ->
                ClientViewModel(updateWishlists, updateInfo, imageStorage) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(GiftViewModel::class.java) ->
                GiftViewModel(updateWishlists, imageStorage) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(StartViewModel::class.java) ->
                StartViewModel(getClientByVkId, loadFriendsVK, addClient) as? T
                    ?: throw IllegalArgumentException(error)
            else ->
                throw IllegalArgumentException(error)
        }
}
