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
import coil.api.load
import com.example.giftgiver.databinding.FragmentUserBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.autoCleared

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private val args: UserFragmentArgs by navArgs()
    private var client: Client? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        client = args.client
        client?.let { bindInfo(it) }
    }

    private fun bindInfo(client: Client) {
        with(binding)
        {
            //todo add favorite menu
            setHasOptionsMenu(true)
            (activity as MainActivity).supportActionBar?.title = client.user.name
            ivAvatar.load(client.user.info.photoMax)
            tvBirthdate.text = client.user.info.bdate
            tvInfo.text = client.user.info.about
            tvName.text = client.user.name
            initWishlists(client.wishlists)
        }
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToWishlist = { position: Int ->
            navigateToWishlist(wishlists[position])
        }
        wishlistAdapter = WishlistAdapter(goToWishlist, null, wishlists)
        with(binding.rvWishlists) {
            adapter = wishlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
        wishlistAdapter.submitList(wishlists)
    }

    private fun navigateToWishlist(wishlist: Wishlist) {
        client?.let {
            val action =
                UserFragmentDirections.actionUserFragmentToFriendsWishlistFragment(
                    wishlist,
                    it.user.name
                )
            findNavController().navigate(action)
        }
    }
}
