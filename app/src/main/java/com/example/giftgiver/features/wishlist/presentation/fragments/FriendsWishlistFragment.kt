package com.example.giftgiver.features.wishlist.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentFriendsWishlistBinding
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.features.wishlist.presentation.viewModels.FriendsWishlistViewModel
import com.example.giftgiver.utils.autoCleared
import javax.inject.Inject

class FriendsWishlistFragment : Fragment() {
    lateinit var binding: FragmentFriendsWishlistBinding
    private val args: FriendsWishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var wishlistIndex = -1
    private var isAdapterInited = false

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var friendsWishlistViewModel: FriendsWishlistViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        friendsWishlistViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[FriendsWishlistViewModel::class.java]
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
        initAdapter(friend.wishlists[wishlistIndex].gifts)
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { position: Int ->
            navigateToItem(position)
        }
        giftAdapter =
            GiftAdapter(goToItem, gifts, ::checkedFunc, friendsWishlistViewModel.getClientCart())
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
        isAdapterInited = false
        val action =
            FriendsWishlistFragmentDirections.actionFriendsWishlistFragmentToMyGiftFragment(
                giftIndex,
                args.friendVkId,
                wishlistIndex
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
                    if (isAdapterInited) {
                        giftAdapter.submitList(it.wishlists[wishlistIndex].gifts)
                    } else {
                        isAdapterInited = true
                        bindInfo(it)
                    }
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
