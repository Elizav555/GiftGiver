package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentUserBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.UserInfo
import com.example.giftgiver.domain.entities.Wishlist
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.wishlist.WishlistAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.autoCleared
import kotlinx.coroutines.launch

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private val args: UserFragmentArgs by navArgs()
    private var isFav = false
    private var friend: Client? = null
    private val clients = ClientsRepositoryImpl(FBMapper())
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
        lifecycleScope.launch {
            friend = clients.getClientByVkId(args.vkId)
            friend?.let {
                bindInfo(it.info)
                initWishlists(it.wishlists)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fav, menu)
        ClientState.client?.let {
            isFav = it.favFriends.contains(friend)
            changeFavBtn(menu.findItem(R.id.fav))
        }
    }

    private fun changeFavBtn(item: MenuItem) {
        item.icon = if (isFav)
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_filed,
                null
            ) else ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_fav_border,
            null
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fav -> {
                onFavClick(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onFavClick(item: MenuItem) = ClientState.client?.let {
        isFav = !isFav
        changeFavBtn(item)
        if (isFav) {
            friend?.let { friend -> it.favFriends.add(friend) }
        } else friend?.let { friend -> it.favFriends.remove(friend) }
        lifecycleScope.launch {
            clients.updateFavFriends(it.vkId, it.favFriends)
        }
    }

    private fun bindInfo(info: UserInfo) {
        with(binding)
        {
            setHasOptionsMenu(true)
            (activity as MainActivity).supportActionBar?.title = info.name
            ivAvatar.load(info.photo)
            tvBirthdate.text = info.bdate
            tvInfo.text = info.about
            tvName.text = info.name
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToWishlist = { position: Int ->
            navigateToWishlist(position)
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

    private fun navigateToWishlist(wishlistIndex: Int) {
        friend?.let {
            val action =
                UserFragmentDirections.actionUserFragmentToFriendsWishlistFragment(
                    wishlistIndex,
                    it
                )
            findNavController().navigate(action)
        }
    }
}
