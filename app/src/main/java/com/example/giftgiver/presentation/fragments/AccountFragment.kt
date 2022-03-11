package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK

class AccountFragment : Fragment() {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private var client: User? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadUserInfoVK().loadInfo(VK.getUserId().value, ::bindInfo)
    }

    private fun bindInfo(newClient: User) {
        client = newClient
        client?.let {
            with(binding) {
                toolbar.inflateMenu(R.menu.menu_edit)
                btnLogout.setOnClickListener {
                    VK.logout()
                }
                ivAvatar.load(it.info?.photoMax)
                tvBirthdate.text = it.info?.bdate
                tvInfo.text = it.info?.about
                tvName.text = it.name

                progressBar.visibility = View.GONE
                views.visibility = View.VISIBLE
            }
        }
    }

    fun addWishlist(wishlist: Wishlist) {
        wishlist.id = client?.info?.wishlists?.size?.toLong() ?: 0
        client?.info?.wishlists?.add(wishlist)
        wishlistAdapter.submitList(client?.info?.wishlists)
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToProfile = { position: Int ->
            navigateToWishlist(wishlists[position].id)
        }

        wishlistAdapter = WishlistAdapter(goToProfile, wishlists)
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

    private fun navigateToWishlist(wishistId: Long) {

    }
}
