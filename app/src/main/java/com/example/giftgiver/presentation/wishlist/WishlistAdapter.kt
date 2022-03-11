package com.example.giftgiver.presentation.wishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemWishlistBinding
import com.example.giftgiver.domain.entities.Wishlist

class WishlistAdapter(
    private val action: (position: Int) -> Unit,
    private val wishlists: List<Wishlist>,
) : ListAdapter<Wishlist, WishlistHolder>(WishlistDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistHolder = WishlistHolder(
        ItemWishlistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action
    )

    override fun onBindViewHolder(
        holder: WishlistHolder,
        position: Int
    ) {
        val wishlist = wishlists[position]
        holder.bind(wishlist)
    }

    override fun submitList(list: List<Wishlist>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
