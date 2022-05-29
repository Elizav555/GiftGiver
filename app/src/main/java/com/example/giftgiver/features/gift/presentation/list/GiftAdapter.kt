package com.example.giftgiver.features.gift.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.domain.GiftInfo

class GiftAdapter(
    private val action: (id: String) -> Unit,
    private val checkedFunc: ((String, Boolean) -> Unit)? = null,
    private val clientCart: List<GiftInfo>? = null
) : ListAdapter<Gift, GiftHolder>(GiftDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GiftHolder = GiftHolder(
        ItemGiftBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action,
        checkedFunc,
        clientCart
    )

    override fun onBindViewHolder(
        holder: GiftHolder,
        position: Int
    ) {
        val gift = getItem(position)
        holder.bind(gift)
    }

    override fun submitList(list: List<Gift>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
