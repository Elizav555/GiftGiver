package com.example.giftgiver.presentation.user

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemUserBinding
import com.example.giftgiver.domain.entities.User

class UserHolder(
    private val binding: ItemUserBinding,
    private val action: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun bind(user: User) {
        with(binding) {
            tvUserName.text = user.name
            ivUserPhoto.load(user.photo)
            root.setOnClickListener {
                action(adapterPosition)
            }
        }
    }
}
