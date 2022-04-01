package com.example.giftgiver.features.user.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentUserBinding
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.presentation.viewModels.UserViewModel
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.list.WishlistAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.autoCleared
import javax.inject.Inject

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private val args: UserFragmentArgs by navArgs()
    private var isFav = false
    private var friend: Client? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        App.mainComponent.inject(this)
        userViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[UserViewModel::class.java]
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        userViewModel.getFriend(args.vkId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fav, menu)
        ClientState.client?.let {
            isFav = it.favFriends.contains(friend)
            changeFavBtn(menu.findItem(R.id.fav))
        }
    }

    private fun changeFavBtn(item: MenuItem) {
        item.icon = if (isFav) {
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_filed,
                null
            )
        } else ResourcesCompat.getDrawable(
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

    private fun onFavClick(item: MenuItem) {
        isFav = !isFav
        changeFavBtn(item)
        userViewModel.updateFavFriends(isFav)
    }

    private fun bindInfo(info: UserInfo) {
        with(binding) {
            setHasOptionsMenu(true)
            (activity as MainActivity).supportActionBar?.title = info.name
            ivAvatar.load(info.photo)
            tvBirthdate.text = info.bdate
            tvInfo.text = info.about
            tvInfo.movementMethod = ScrollingMovementMethod()
            tvName.movementMethod = ScrollingMovementMethod()
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
            val action = UserFragmentDirections.actionUserFragmentToFriendsWishlistFragment(
                wishlistIndex,
                it.vkId
            )
            findNavController().navigate(action)
        }
    }

    private fun initObservers() {
        userViewModel.friend.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { client ->
                friend = client
                friend?.let {
                    bindInfo(it.info)
                    initWishlists(it.wishlists)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
