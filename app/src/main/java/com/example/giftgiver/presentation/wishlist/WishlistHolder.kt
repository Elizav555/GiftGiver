package com.example.giftgiver.presentation.wishlist

import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.databinding.ItemWishlistBinding
import com.example.giftgiver.domain.entities.Wishlist

class WishlistHolder(
    private val binding: ItemWishlistBinding,
    action: (position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(Wishlist: Wishlist) {
        with(binding) {
            tvWishlistName.text = Wishlist.name
        }
    }
}
