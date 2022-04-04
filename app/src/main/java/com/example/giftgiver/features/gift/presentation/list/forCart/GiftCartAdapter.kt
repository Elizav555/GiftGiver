package com.example.giftgiver.features.gift.presentation.list.forCart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemGiftCartBinding
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftDiffItemCallback

class GiftCartAdapter(
    private val action: (id: String) -> Unit,
    private val gifts: List<Gift>,
    private val clientStateRep: ClientStateRep
) : ListAdapter<Gift, GiftCartHolder>(GiftDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GiftCartHolder = GiftCartHolder(
        ItemGiftCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action,
        clientStateRep
    )

    override fun onBindViewHolder(
        holder: GiftCartHolder,
        position: Int
    ) {
        val gift = getItem(position)
        holder.bind(gift)
    }

    override fun submitList(list: List<Gift>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
