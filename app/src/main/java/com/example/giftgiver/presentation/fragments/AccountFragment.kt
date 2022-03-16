package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private val clients = ClientsRepositoryImpl(fbMapper = FBMapper())
    private val client = ClientState.client
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        client?.let { bindInfo(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    private fun bindInfo(curClient: Client) {
        with(binding) {
            setHasOptionsMenu(true)
            btnLogout.setOnClickListener {
                logout()
            }
            btnAdd.setOnClickListener {
                AddWishlistDialogFragment()
                    .show(childFragmentManager, "dialog")
            }
            ivAvatar.load(curClient.user.info.photoMax)
            tvBirthdate.text = curClient.user.info.bdate
            tvInfo.text = curClient.user.info.about
            tvName.text = curClient.user.name
            initWishlists(curClient.user.info.wishlists)
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }

    fun addWishlist(wishlist: Wishlist) {
        client?.let {
            client.user.info.wishlists.add(wishlist)
            lifecycleScope.launch {
                clients.updateClient(client.vkId, mapOf("wishlists" to client.user.info.wishlists))
            }
            wishlistAdapter.submitList(client.user.info.wishlists)
        }
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToWishlist = { position: Int ->
            navigateToWishlist(position)
        }
        val delete = { position: Int ->
            deleteWishlist(wishlists[position])
        }
        wishlistAdapter = WishlistAdapter(goToWishlist, delete, wishlists)
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

    private fun deleteWishlist(wishlist: Wishlist) {
        client?.let {
            client.user.info.wishlists.remove(wishlist)
            lifecycleScope.launch {
                clients.updateClient(client.vkId, mapOf("wishlists" to client.user.info.wishlists))
            }
            wishlistAdapter.submitList(client.user.info.wishlists)
        }
    }

    private fun navigateToWishlist(wishistIndex: Int) {
        val action = AccountFragmentDirections.actionAccountToMyWishlistFragment(wishistIndex)
        findNavController().navigate(action)
    }

    private fun logout() {
        VK.logout()
        ClientState.client = null
        findNavController().navigate(AccountFragmentDirections.actionAccountToStartFragment())

    }
}
