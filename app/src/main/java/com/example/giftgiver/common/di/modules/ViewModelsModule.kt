package com.example.giftgiver.common.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.giftgiver.common.viewModels.MainViewModel
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.common.viewModels.ViewModelKey
import com.example.giftgiver.features.calendar.presentation.CalendarViewModel
import com.example.giftgiver.features.cart.presentation.CartViewModel
import com.example.giftgiver.features.client.presentation.ClientViewModel
import com.example.giftgiver.features.gift.presentation.GiftViewModel
import com.example.giftgiver.features.images.presentation.ImageViewModel
import com.example.giftgiver.features.start.presentation.StartViewModel
import com.example.giftgiver.features.user.presentation.viewModels.FriendsViewModel
import com.example.giftgiver.features.user.presentation.viewModels.UserViewModel
import com.example.giftgiver.features.wishlist.presentation.viewModels.FriendsWishlistViewModel
import com.example.giftgiver.features.wishlist.presentation.viewModels.WishlistViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelsModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(
        viewModel: CartViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CalendarViewModel::class)
    abstract fun bindCalendarViewModel(
        viewModel: CalendarViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ClientViewModel::class)
    abstract fun bindClientViewModel(
        viewModel: ClientViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GiftViewModel::class)
    abstract fun bindGiftViewModel(
        viewModel: GiftViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    abstract fun bindStartViewModel(
        viewModel: StartViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(
        viewModel: UserViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    abstract fun bindFriendsViewModel(
        viewModel: FriendsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WishlistViewModel::class)
    abstract fun bindWishlistViewModel(
        viewModel: WishlistViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FriendsWishlistViewModel::class)
    abstract fun bindFriendsWishlistViewModel(
        viewModel: FriendsWishlistViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    abstract fun bindImageViewModel(
        viewModel: ImageViewModel
    ): ViewModel
}
