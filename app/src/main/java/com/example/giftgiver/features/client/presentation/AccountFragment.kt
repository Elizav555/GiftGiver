package com.example.giftgiver.features.client.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.dialogs.AddWishlistDialog
import com.example.giftgiver.features.wishlist.presentation.list.WishlistAdapter
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.autoCleared
import com.example.giftgiver.utils.viewModel
import java.io.File

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private var isAdapterInited = false
    private val clientViewModel: ClientViewModel by viewModel()
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
        clientViewModel.getClient()
            ?.let { EditClientDialog(it).show(childFragmentManager, "dialog") }
    }

    private fun bindAll() {
        with(binding) {
            setHasOptionsMenu(true)
            btnLogout.setOnClickListener {
                clientViewModel.logout()
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
