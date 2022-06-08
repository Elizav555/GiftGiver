package com.example.giftgiver.features.gift.presentation.list

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo

class GiftHolder(
    private val binding: ItemGiftBinding,
    private val action: (id: String) -> Unit,
    private val checkedFunc: ((String, Boolean) -> Unit)?,
    private val clientCart: List<GiftInfo>? = null
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gift: Gift) {
        with(binding) {
            root.setOnClickListener { action(gift.id) }
            checkedFunc?.let {
                checkBox.isVisible = true
                checkBox.isChecked = gift.isChosen
                if (gift.isChosen) {
                    val isYours = clientCart?.any { it.giftId == gift.id } == true
                    ivFilter.isVisible = !isYours
                    ivFilterClient.isVisible = isYours
                    checkBox.isClickable = isYours
                }
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    it(gift.id, isChecked)
                    checkBox.setOnCheckedChangeListener(null)
                }
            }
            tvName.text = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.default_gift)
            }
        }
    }
}
