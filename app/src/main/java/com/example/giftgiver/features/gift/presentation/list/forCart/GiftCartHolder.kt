package com.example.giftgiver.features.gift.presentation.list.forCart

import androidx.core.view.isVisible
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
            ivChanged.isVisible = gift.isChanged
            tvFor.text = gift.forName
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
