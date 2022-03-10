package com.example.giftgiver.presentation.user

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.domain.entities.User

class UserDiffItemCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean = oldItem.vkId == newItem.vkId

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean = oldItem == newItem
}
