package com.example.giftgiver.features.wishlist.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsWishlistBinding
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.features.wishlist.presentation.viewModels.FriendsWishlistViewModel
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.autoCleared
import com.example.giftgiver.utils.viewModel

class FriendsWishlistFragment : BaseFragment(R.layout.fragment_friends_wishlist) {
    lateinit var binding: FragmentFriendsWishlistBinding
    private val args: FriendsWishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var wishlistIndex = -1
    private var isAdapterInited = false
    private val friendsWishlistViewModel: FriendsWishlistViewModel by viewModel()
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
        initObservers()
        friendsWishlistViewModel.getFriend(args.friendVkId)
        wishlistIndex = args.wishlistIndex
    }

    private fun bindInfo(friend: Client) {
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.title =
            getString(
                R.string.friend_wishlist,
                friend.info.name,
                friend.wishlists[wishlistIndex].name
            )
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { id: String ->
            navigateToItem(id)
        }
        giftAdapter =
            GiftAdapter(goToItem, ::checkedFunc, friendsWishlistViewModel.getClientCart())
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

    private fun navigateToItem(giftId: String) {
        isAdapterInited = false
        val action =
            FriendsWishlistFragmentDirections.actionFriendsWishlistFragmentToMyGiftFragment(
                giftId,
                args.friendVkId,
            )
        findNavController().navigate(action)
    }

    private fun checkedFunc(pos: Int, isChecked: Boolean) {
        friendsWishlistViewModel.checkedFunc(pos, isChecked, wishlistIndex)
    }

    private fun initObservers() {
        friendsWishlistViewModel.friend.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { friend ->
                friend?.let {
                    bindInfo(it)
                    friendsWishlistViewModel.getGifts(it.vkId, it.wishlists[wishlistIndex].giftsIds)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
        friendsWishlistViewModel.gifts.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                if (isAdapterInited) {
                    giftAdapter.submitList(it)
                } else {
                    isAdapterInited = true
                    initAdapter(it as MutableList<Gift>)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
