package com.example.giftgiver.presentation.user

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemUserBinding
import com.example.giftgiver.domain.entities.UserInfo

class UserHolder(
    private val binding: ItemUserBinding,
    private val action: (id: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserInfo) {
        with(binding) {
            tvUserName.text = user.name
            ivUserPhoto.load(user.photo)
            root.setOnClickListener {
                action(user.vkId)
            }
        }
    }
}
