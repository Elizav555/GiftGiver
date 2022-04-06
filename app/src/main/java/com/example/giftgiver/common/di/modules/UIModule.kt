package com.example.giftgiver.common.di.modules

import com.example.giftgiver.MainActivity
import com.example.giftgiver.common.di.scopes.ActivityScope
import com.example.giftgiver.features.calendar.presentation.CalendarFragment
import com.example.giftgiver.features.cart.presentation.CartFragment
import com.example.giftgiver.features.client.presentation.AccountFragment
import com.example.giftgiver.features.client.presentation.EditClientDialog
import com.example.giftgiver.features.gift.presentation.AddGiftDialog
import com.example.giftgiver.features.gift.presentation.GiftFragment
import com.example.giftgiver.features.start.presentation.StartFragment
import com.example.giftgiver.features.user.presentation.FriendsListFragment
import com.example.giftgiver.features.user.presentation.UserFragment
import com.example.giftgiver.features.wishlist.presentation.fragments.FriendsWishlistFragment
import com.example.giftgiver.features.wishlist.presentation.fragments.WishlistFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeCartFragment(): CartFragment

    @ContributesAndroidInjector
    abstract fun contributeCalendarFragment(): CalendarFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector
    abstract fun contributeEditClientDialog(): EditClientDialog

    @ContributesAndroidInjector
    abstract fun contributeAddGiftDialog(): AddGiftDialog

    @ContributesAndroidInjector
    abstract fun contributeGiftFragment(): GiftFragment

    @ContributesAndroidInjector
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeFriendsListFragment(): FriendsListFragment

    @ContributesAndroidInjector
    abstract fun contributeWishlistFragment(): WishlistFragment

    @ContributesAndroidInjector
    abstract fun contributeFriendsWishlistFragment(): FriendsWishlistFragment
}
