package com.example.giftgiver.features.gift.presentation.list.forCart

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.ItemGiftCartBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo

class GiftCartHolder(
    private val binding: ItemGiftCartBinding,
    private val action: (id: String) -> Unit,
    private val checkIsVisible: (id: String) -> Boolean
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gift: Gift) {
        with(binding) {
            ivChanged.isVisible = checkIsVisible(gift.id)
            tvFor.text = gift.forName
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl){
                crossfade(true)
                placeholder(R.drawable.default_gift)
            }
            root.setOnClickListener {
                action(gift.id)
            }
        }
    }
}
