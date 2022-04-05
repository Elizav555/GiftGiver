package com.example.giftgiver.features.client.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.user.domain.FriendsStateRep
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.dialogs.AddWishlistDialog
import com.example.giftgiver.features.wishlist.presentation.list.WishlistAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import java.io.File
import javax.inject.Inject

class AccountFragment : Fragment() {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private var isAdapterInited = false

    @Inject
    lateinit var clientStateRep: ClientStateRep

    @Inject
    lateinit var friendsStateRep: FriendsStateRep

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var clientViewModel: ClientViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clientViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[ClientViewModel::class.java]
        initObservers()
        clientViewModel.getInfo()
        clientViewModel.getWishlists()
        bindAll()
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
        clientStateRep.getClient()
            ?.let { EditClientDialog(it).show(childFragmentManager, "dialog") }
    }

    private fun bindAll() {
        with(binding) {
            setHasOptionsMenu(true)
            btnLogout.setOnClickListener {
                logout()
            }
            btnAdd.setOnClickListener {
                AddWishlistDialog()
                    .show(childFragmentManager, "dialog")
            }
            tvInfo.movementMethod = ScrollingMovementMethod()
            tvName.movementMethod = ScrollingMovementMethod()
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }

    private fun bindInfo(userInfo: UserInfo) =
        with(userInfo) {
            binding.ivAvatar.load(photo)
            binding.tvBirthdate.text = bdate
            binding.tvInfo.text = about
            binding.tvName.text = name
        }

    fun addWishlist(wishlist: Wishlist) = clientViewModel.addWishlist(wishlist)

    private fun initAdapter(wishlists: List<Wishlist>) {
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

    private fun deleteWishlist(wishlist: Wishlist) = clientViewModel.deleteWishlist(wishlist)

    private fun navigateToWishlist(wishlistIndex: Int) {
        isAdapterInited = false
        val action =
            AccountFragmentDirections.actionAccountToMyWishlistFragment(
                wishlistIndex
            )
        findNavController().navigate(action)
    }

    private fun logout() { //todo check mb change
        VK.logout()
        clientStateRep.deleteClient()
        friendsStateRep.deleteFriends()
        isAdapterInited = false
        findNavController().navigate(AccountFragmentDirections.actionAccountToStartFragment())
    }

    fun updateInfo(newName: String, newInfo: String, newBirthDate: String, imageFile: File?) =
        clientViewModel.updateInfo(newName, newInfo, newBirthDate, imageFile)

    private fun initObservers() {
        clientViewModel.wishlists.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val wishlists = it
                if (isAdapterInited) {
                    wishlistAdapter.submitList(wishlists)
                } else {
                    isAdapterInited = true
                    initAdapter(wishlists)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }

        clientViewModel.info.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { info ->
                info?.let { bindInfo(it) }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
