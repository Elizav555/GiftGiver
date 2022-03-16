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
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.autoCleared

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private val args: UserFragmentArgs by navArgs()
    private var user: User? = null
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
        user = args.user
        user?.let { bindInfo(it) }
    }

    private fun bindInfo(user: User) {
        with(binding)
        {
            //todo add favorite menu
            setHasOptionsMenu(true)
            (activity as MainActivity).supportActionBar?.title = user.name
            ivAvatar.load(user.info.photoMax)
            tvBirthdate.text = user.info.bdate
            tvInfo.text = user.info.about
            tvName.text = user.name
            initWishlists(user.info.wishlists)
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
        user?.let {
            val action =
                UserFragmentDirections.actionUserFragmentToFriendsWishlistFragment(
                    wishlist,
                    it.name
                )
            findNavController().navigate(action)
        }
    }
}
