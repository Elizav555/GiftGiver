package com.example.giftgiver.presentation.wishlist

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.domain.entities.Wishlist

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
