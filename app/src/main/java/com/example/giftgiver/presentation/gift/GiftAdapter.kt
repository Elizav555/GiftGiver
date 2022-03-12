package com.example.giftgiver.presentation.gift

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.domain.entities.Gift

class GiftAdapter(
    private val action: (position: Int) -> Unit,
    private val gifts: List<Gift>,
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
        action
    )

    override fun onBindViewHolder(
        holder: GiftHolder,
        position: Int
    ) {
        val gift = gifts[position]
        holder.bind(gift)
    }

    override fun submitList(list: List<Gift>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}