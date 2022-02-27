package com.example.giftgiver.user

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemUserBinding

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
            userNameTv.text = user.firstName +" "+ user.lastName
            userPhotoIv.load(user.photo)
        }
    }
}
