package com.example.giftgiver.features.gift.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.features.gift.domain.Gift

class GiftHolder(
    private val binding: ItemGiftBinding,
    action: (position: Int) -> Unit,
    private val checkedFunc: ((Int, Boolean) -> Unit)?,
    private val clientCart: List<Gift>?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(gift: Gift) {
        with(binding) {
            checkedFunc?.let {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = gift.isChosen
                if (gift.isChosen) {
                    ivFilter.visibility = View.VISIBLE
                    checkBox.isClickable = clientCart?.contains(gift) == true
                }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    it(adapterPosition, isChecked)
                }
            }
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
