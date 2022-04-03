package com.example.giftgiver.features.gift.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.features.gift.domain.Gift

class GiftHolder(
    private val binding: ItemGiftBinding,
    private val action: (id: String) -> Unit,
    private val checkedFunc: ((Int, Boolean) -> Unit)?,
    private val clientCart: List<Pair<String, Long>>? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gift: Gift) {
        with(binding) {
            root.setOnClickListener { action(gift.id) }
            checkedFunc?.let {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = gift.isChosen
                if (gift.isChosen) {
                    ivFilter.visibility = View.VISIBLE
                    checkBox.isClickable = clientCart?.contains(gift.id to gift.forId) == true
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
