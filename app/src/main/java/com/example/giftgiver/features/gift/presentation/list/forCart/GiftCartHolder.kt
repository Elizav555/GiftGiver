package com.example.giftgiver.features.gift.presentation.list.forCart

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftCartBinding
import com.example.giftgiver.features.gift.domain.Gift

class GiftCartHolder(
    private val binding: ItemGiftCartBinding,
    action: (position: Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(gift: Gift) {
        with(binding) {
            tvFor.text = gift.forName
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
