package com.example.giftgiver.presentation.gift

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.domain.entities.Gift

class GiftHolder(
    private val binding: ItemGiftBinding,
    action: (position: Int) -> Unit,
    private val isFriends: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(gift: Gift) {
        with(binding) {
            if (isFriends) {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = gift.isChosen
                if (gift.isChosen) {
                    ivFilter.visibility = View.VISIBLE
                    checkBox.isClickable = false
                }
            }
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
