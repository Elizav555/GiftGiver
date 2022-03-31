package com.example.giftgiver.common.di.components

import com.example.giftgiver.common.di.scopes.ActivityScope
import com.example.giftgiver.MainActivity
import com.example.giftgiver.features.calendar.presentation.CalendarFragment
import com.example.giftgiver.features.cart.presentation.CartFragment
import com.example.giftgiver.features.client.presentation.AccountFragment
import com.example.giftgiver.features.gift.presentation.AddGiftDialog
import com.example.giftgiver.features.client.presentation.EditClientDialog
import com.example.giftgiver.features.gift.presentation.GiftFragment
import com.example.giftgiver.features.start.presentation.StartFragment
import com.example.giftgiver.features.user.presentation.FriendsListFragment
import com.example.giftgiver.features.user.presentation.UserFragment
import com.example.giftgiver.features.wishlist.presentation.fragments.FriendsWishlistFragment
import com.example.giftgiver.features.wishlist.presentation.fragments.WishlistFragment
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    fun inject(accountFragment: AccountFragment)
    fun inject(calendarFragment: CalendarFragment)
    fun inject(cartFragment: CartFragment)
    fun inject(friendsListFragment: FriendsListFragment)
    fun inject(friendsWishlistFragment: FriendsWishlistFragment)
    fun inject(giftFragment: GiftFragment)
    fun inject(startFragment: StartFragment)
    fun inject(userFragment: UserFragment)
    fun inject(wishlistFragment: WishlistFragment)

    fun inject(addGiftDialog: AddGiftDialog)
    fun inject(editClientDialog: EditClientDialog)
}
