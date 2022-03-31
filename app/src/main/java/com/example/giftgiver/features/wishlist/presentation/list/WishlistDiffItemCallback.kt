package com.example.giftgiver.features.wishlist.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.features.wishlist.domain.Wishlist

class WishlistDiffItemCallback : DiffUtil.ItemCallback<Wishlist>() {
    override fun areItemsTheSame(
        oldItem: Wishlist,
        newItem: Wishlist
    ): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(
        oldItem: Wishlist,
        newItem: Wishlist
    ): Boolean = oldItem == newItem
}
