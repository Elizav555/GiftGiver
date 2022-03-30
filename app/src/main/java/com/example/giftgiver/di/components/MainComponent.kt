package com.example.giftgiver.di.components

import com.example.giftgiver.di.ActivityScope
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.fragments.*
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
}
