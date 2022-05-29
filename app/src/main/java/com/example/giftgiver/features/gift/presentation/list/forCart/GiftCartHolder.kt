package com.example.giftgiver.features.gift.presentation.list.forCart

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.giftgiver.databinding.ItemGiftCartBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo

class GiftCartHolder(
    private val binding: ItemGiftCartBinding,
    private val action: (id: String) -> Unit,
    private val gifts: List<GiftInfo>?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gift: Gift) {
        with(binding) {
            ivChanged.isVisible =
                gift.lastChanged.after(gifts?.firstOrNull { it.giftId == gift.id }?.lastSeen)
            tvFor.text = gift.forName
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
            root.setOnClickListener {
                action(gift.id)
            }
        }
    }
}
