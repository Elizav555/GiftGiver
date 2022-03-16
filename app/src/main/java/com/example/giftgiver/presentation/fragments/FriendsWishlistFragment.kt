package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsWishlistBinding
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.gift.GiftAdapter
import com.example.giftgiver.utils.autoCleared

class FriendsWishlistFragment : Fragment() {
    lateinit var binding: FragmentFriendsWishlistBinding
    private val args: FriendsWishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var wishlist: Wishlist? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsWishlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wishlist = args.wishlist
        wishlist?.let { bindInfo(it) }
    }

    private fun bindInfo(wishlist: Wishlist) {
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.title =
            getString(R.string.friend_wishlist, args.friendName, wishlist.name)
        initAdapter(wishlist.gifts)
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { position: Int ->
            navigateToItem(position)
        }
        giftAdapter = GiftAdapter(goToItem, gifts, true)
        with(binding.rvGifts) {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
        giftAdapter.submitList(gifts)
    }

    private fun navigateToItem(giftIndex: Int) {
        wishlist?.let {
            val action =
                FriendsWishlistFragmentDirections.actionFriendsWishlistFragmentToMyGiftFragment(
                    giftIndex,
                    it.gifts.toTypedArray(),
                    false,
                    -1
                )
            findNavController().navigate(action)
        }
    }
}
