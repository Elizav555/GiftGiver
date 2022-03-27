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
import com.example.giftgiver.data.firebase.ImageStorage
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.dialogs.AddWishlistDialog
import com.example.giftgiver.presentation.dialogs.EditClientDialog
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch
import java.io.File

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
        client?.let { bindAll(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit -> {
                enterEditMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun enterEditMode() {
        ClientState.client?.let { EditClientDialog(it).show(childFragmentManager, "dialog") }
    }

    private fun bindAll(curClient: Client) {
        with(binding) {
            setHasOptionsMenu(true)
            btnLogout.setOnClickListener {
                logout()
            }
            btnAdd.setOnClickListener {
                AddWishlistDialog()
                    .show(childFragmentManager, "dialog")
            }
            bindInfo()
            initWishlists(curClient.wishlists)
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }

    private fun bindInfo() = client?.let {
        with(it.info) {
            binding.ivAvatar.load(photo)
            binding.tvBirthdate.text = bdate
            binding.tvInfo.text = about
            binding.tvName.text = name
        }
    }

    fun addWishlist(wishlist: Wishlist) {
        client?.let {
            it.wishlists.add(wishlist)
            lifecycleScope.launch {
                clients.updateWishlists(it.vkId, it.wishlists)
            }
            wishlistAdapter.submitList(it.wishlists)
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
            it.wishlists.remove(wishlist)
            lifecycleScope.launch {
                clients.updateWishlists(client.vkId, it.wishlists)
            }
            wishlistAdapter.submitList(it.wishlists)
        }
    }

    private fun navigateToWishlist(wishlistIndex: Int) {
        val action = AccountFragmentDirections.actionAccountToMyWishlistFragment(wishlistIndex)
        findNavController().navigate(action)
    }

    private fun logout() {
        VK.logout()
        ClientState.client = null
        findNavController().navigate(AccountFragmentDirections.actionAccountToStartFragment())
    }

    fun updateInfo(newName: String, newInfo: String, newBirthDate: String, imageFile: File?) =
        client?.let {
            lifecycleScope.launch {
                imageFile?.let { file ->
                    it.info.photo = ImageStorage().addImage(file).toString()
                }
                it.info.name = newName
                it.info.about = newInfo
                it.info.bdate = newBirthDate
                clients.updateInfo(it.vkId, it.info)
                ClientState.client = it
                bindInfo()
            }
        }
}
