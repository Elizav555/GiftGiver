package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.autoCleared
import com.google.firebase.firestore.ktx.toObject
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private val clients = ClientsRepositoryImpl()
    private lateinit var client: Client
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
        lifecycleScope.launch {
            clients.getClientByVkId(VK.getUserId().value).addOnSuccessListener {
                it.toObject<ClientFB>()?.let { client ->
                    lifecycleScope.launch { bindInfo(FBMapper().mapClientFromFB(client)) }
                }
            }
        }
    }

    private fun bindInfo(newClient: Client) {
        client = newClient
        with(binding) {
            toolbar.inflateMenu(R.menu.menu_edit)
            btnLogout.setOnClickListener {
                VK.logout()
                //todo login into vk
            }
            btnAdd.setOnClickListener {
                AddWishlistDialogFragment()
                    .show(childFragmentManager, "dialog")
            }
            ivAvatar.load(client.user.info.photoMax)
            tvBirthdate.text = client.user.info.bdate
            tvInfo.text = client.user.info.about
            tvName.text = client.user.name
            initWishlists(client.user.info.wishlists)
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }

    fun addWishlist(wishlist: Wishlist) {
        client.user.info.wishlists.add(wishlist)
        lifecycleScope.launch {
            clients.updateClient(client.vkId, mapOf("wishlists" to client.user.info.wishlists))
        }
        wishlistAdapter.submitList(client.user.info.wishlists)
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToProfile = { position: Int ->
            navigateToWishlist(wishlists[position])
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

    private fun navigateToWishlist(wishist: Wishlist) {
        //todo navigate
    }
}
