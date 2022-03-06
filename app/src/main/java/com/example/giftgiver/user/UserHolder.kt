package com.example.giftgiver.user

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemUserBinding
import com.example.giftgiver.entities.User

class UserHolder(
    private val binding: ItemUserBinding,
    action: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(user: User) {
        with(binding) {
            ivUserPhoto.clipToOutline = true
            tvUserName.text = user.name
            ivUserPhoto.load(user.photo)
        }
    }
}
