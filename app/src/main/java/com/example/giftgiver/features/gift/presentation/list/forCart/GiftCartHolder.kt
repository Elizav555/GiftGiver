package com.example.giftgiver.features.gift.presentation.list.forCart

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.databinding.ItemGiftCartBinding
import com.example.giftgiver.features.client.domain.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import java.util.*

class GiftCartHolder(
    private val binding: ItemGiftCartBinding,
    private val action: (id: String) -> Unit,
    private val clientStateRep: ClientStateRep
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(gift: Gift) {
        with(binding) {
            val client = clientStateRep.getClient()
            ivChanged.isVisible =
                gift.lastChanged.after(client?.cart?.giftsInfo?.first { it.giftId == gift.id }?.lastSeen)
            tvFor.text = gift.forName
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
            root.setOnClickListener {
                client?.cart?.giftsInfo?.first { it.giftId == gift.id }?.lastSeen =
                    Calendar.getInstance()
                client?.let { clientStateRep.addClient(client) }
                action(gift.id)
            }
        }
    }
}
