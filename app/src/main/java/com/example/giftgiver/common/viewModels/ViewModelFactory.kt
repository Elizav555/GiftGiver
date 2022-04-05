package com.example.giftgiver.common.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.common.db.fileStorage.ImageStorage
import com.example.giftgiver.features.calendar.domain.useCases.GetHolidaysUseCase
import com.example.giftgiver.features.calendar.presentation.CalendarViewModel
import com.example.giftgiver.features.cart.presentation.CartViewModel
import com.example.giftgiver.features.client.domain.useCases.*
import com.example.giftgiver.features.client.presentation.ClientViewModel
import com.example.giftgiver.features.event.data.DateMapper
import com.example.giftgiver.features.gift.domain.useCases.GiftUseCase
import com.example.giftgiver.features.gift.presentation.GiftViewModel
import com.example.giftgiver.features.start.presentation.StartViewModel
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsUseCase
import com.example.giftgiver.features.user.domain.useCases.LoadFriendsVK
import com.example.giftgiver.features.user.presentation.viewModels.FriendsViewModel
import com.example.giftgiver.features.user.presentation.viewModels.UserViewModel
import com.example.giftgiver.features.wishlist.presentation.viewModels.FriendsWishlistViewModel
import com.example.giftgiver.features.wishlist.presentation.viewModels.WishlistViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val getHolidays: GetHolidaysUseCase,
    private val dateMapper: DateMapper,
    private val imageStorage: ImageStorage,
    private val getClientByVkId: GetClientByVkId,
    private val loadFriendsVK: LoadFriendsVK,
    private val addClient: AddClientUseCase,
    private val giftUseCase: GiftUseCase,
    private val clientUpdatedUseCase: ClientUpdatedUseCase,
    private val updateClientStateUseCase: UpdateClientStateUseCase,
    private val updateOfflineClientUseCase: UpdateOfflineClientUseCase,
    private val clientFBUseCase: ClientFBUseCase,
    private val getClientState: GetClientStateUseCase,
    private val loadFriendsUseCase: LoadFriendsUseCase
) : ViewModelProvider.Factory {
    private val error = App.appComponent.getContext().getString(R.string.unknownViewModel)
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) ->
                ImageViewModel() as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(CalendarViewModel::class.java) ->
                CalendarViewModel(
                    getHolidays,
                    clientFBUseCase,
                    dateMapper,
                    getClientState
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(CartViewModel::class.java) ->
                CartViewModel(
                    clientFBUseCase,
                    getClientState,
                    giftUseCase
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(ClientViewModel::class.java) ->
                ClientViewModel(
                    imageStorage,
                    getClientState,
                    clientFBUseCase,
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(GiftViewModel::class.java) ->
                GiftViewModel(
                    getClientByVkId,
                    imageStorage,
                    getClientState,
                    giftUseCase,
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(StartViewModel::class.java) ->
                StartViewModel(getClientByVkId, loadFriendsUseCase, addClient, getClientState) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(UserViewModel::class.java) ->
                UserViewModel(
                    getClientByVkId, clientFBUseCase, getClientState,
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(FriendsViewModel::class.java) ->
                FriendsViewModel(getClientByVkId, getClientState) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(FriendsWishlistViewModel::class.java) ->
                FriendsWishlistViewModel(
                    getClientByVkId,
                    getClientState,
                    giftUseCase,
                    clientFBUseCase,
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(WishlistViewModel::class.java) ->
                WishlistViewModel(
                    imageStorage,
                    getClientState,
                    giftUseCase,
                    clientFBUseCase,
                ) as? T
                    ?: throw IllegalArgumentException(error)
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    clientUpdatedUseCase,
                    updateClientStateUseCase,
                    updateOfflineClientUseCase,
                    getClientByVkId
                ) as? T
                    ?: throw IllegalArgumentException(error)
            else ->
                throw IllegalArgumentException(error)
        }
}
