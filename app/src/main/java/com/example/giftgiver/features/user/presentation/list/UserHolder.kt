package com.example.giftgiver.features.user.presentation.list

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.ItemUserBinding
import com.example.giftgiver.features.user.domain.UserInfo

class UserHolder(
    private val binding: ItemUserBinding,
    private val action: (id: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserInfo) {
        with(binding) {
            tvUserName.text = user.name
            ivUserPhoto.load(user.photo){
                crossfade(true)
                placeholder(R.drawable.default_person)
            }
            root.setOnClickListener {
                action(user.vkId)
            }
        }
    }
}
