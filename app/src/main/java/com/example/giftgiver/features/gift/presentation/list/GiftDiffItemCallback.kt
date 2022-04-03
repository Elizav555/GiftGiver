package com.example.giftgiver.features.gift.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.features.gift.domain.Gift

class GiftDiffItemCallback : DiffUtil.ItemCallback<Gift>() {
    override fun areItemsTheSame(
        oldItem: Gift,
        newItem: Gift
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Gift,
        newItem: Gift
    ): Boolean = oldItem == newItem
}
