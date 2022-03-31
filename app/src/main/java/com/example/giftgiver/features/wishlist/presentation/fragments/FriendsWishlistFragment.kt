package com.example.giftgiver.features.wishlist.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsWishlistBinding
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.autoCleared
import kotlinx.coroutines.launch
import javax.inject.Inject

class FriendsWishlistFragment : Fragment() {
    lateinit var binding: FragmentFriendsWishlistBinding
    private val args: FriendsWishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var wishlistIndex = -1
    private var friend: Client? = null

    @Inject
    lateinit var updateWishlists: UpdateWishlistUseCase

    @Inject
    lateinit var updateCart: UpdateCartUseCase
    private val client = ClientState.client
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        binding = FragmentFriendsWishlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wishlistIndex = args.wishlistIndex
        friend = args.friend
        friend?.let { bindInfo(it.wishlists[wishlistIndex]) }
    }

    private fun bindInfo(wishlist: Wishlist) {
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.title =
            getString(R.string.friend_wishlist, friend?.info?.name, wishlist.name)
        initAdapter(wishlist.gifts)
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { position: Int ->
            navigateToItem(position)
        }
        giftAdapter = GiftAdapter(goToItem, gifts, ::checkedFunc)
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
        friend?.let {
            val action =
                FriendsWishlistFragmentDirections.actionFriendsWishlistFragmentToMyGiftFragment(
                    giftIndex,
                    it.wishlists[wishlistIndex].gifts.toTypedArray(),
                    false,
                    wishlistIndex
                )
            findNavController().navigate(action)
        }
    }

    private fun checkedFunc(pos: Int, isChecked: Boolean) = friend?.let { friend ->
        friend.wishlists[wishlistIndex].gifts[pos].isChosen = isChecked
        val gift = friend.wishlists[wishlistIndex].gifts[pos]
        if (isChecked) client?.cart?.gifts?.add(gift)
        else client?.cart?.gifts?.remove(gift)
        lifecycleScope.launch {
            updateWishlists(friend.vkId, friend.wishlists)
            client?.let { updateCart(it.vkId, it.cart.gifts) }
        }
        giftAdapter.submitList(friend.wishlists[wishlistIndex].gifts)
    }
}
