package com.example.giftgiver.presentation.gift

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.domain.entities.Gift

class GiftHolder(
    private val binding: ItemGiftBinding,
    action: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(gift: Gift) {
        with(binding) {
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
