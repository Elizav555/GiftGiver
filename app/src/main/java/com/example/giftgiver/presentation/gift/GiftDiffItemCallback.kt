package com.example.giftgiver.presentation.gift

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.entities.Wishlist

class GiftDiffItemCallback : DiffUtil.ItemCallback<Gift>() {
    override fun areItemsTheSame(
        oldItem: Gift,
        newItem: Gift
    ): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(
        oldItem: Gift,
        newItem: Gift
    ): Boolean = oldItem == newItem
}
