package com.example.giftgiver.features.user.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.example.giftgiver.features.user.domain.UserInfo

class UserDiffItemCallback : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(
        oldItem: UserInfo,
        newItem: UserInfo
    ): Boolean = oldItem.vkId == newItem.vkId

    override fun areContentsTheSame(
        oldItem: UserInfo,
        newItem: UserInfo
    ): Boolean = oldItem == newItem
}
