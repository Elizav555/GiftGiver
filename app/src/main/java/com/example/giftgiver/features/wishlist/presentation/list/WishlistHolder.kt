package com.example.giftgiver.features.wishlist.presentation.list

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.databinding.ItemWishlistBinding
import com.example.giftgiver.features.wishlist.domain.Wishlist

class WishlistHolder(
    private val binding: ItemWishlistBinding,
    action: (position: Int) -> Unit,
    deleteAction: ((position: Int) -> Unit)?,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
        if (deleteAction == null) {
            binding.btnDelete.isVisible = false
        } else binding.btnDelete.setOnClickListener {
            deleteAction(adapterPosition)
        }
    }

    fun bind(Wishlist: Wishlist) {
        with(binding) {
            tvWishlistName.text = Wishlist.name
        }
    }
}
