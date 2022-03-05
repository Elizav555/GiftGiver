package com.example.giftgiver.user

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.entities.User

class UserDiffItemCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: User,
        newItem: User
    ): Boolean = oldItem == newItem
}
