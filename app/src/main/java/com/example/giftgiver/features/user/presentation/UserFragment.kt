package com.example.giftgiver.features.user.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentUserBinding
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.presentation.viewModels.UserViewModel
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.list.WishlistAdapter
import com.example.giftgiver.utils.*
import javax.inject.Inject

class UserFragment : BaseFragment(R.layout.fragment_user) {
    private lateinit var binding: FragmentUserBinding
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private val args: UserFragmentArgs by navArgs()
    private var isFav = false
    private var friend: Client? = null
    private val userViewModel: UserViewModel by viewModel()

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentUserBinding.inflate(inflater)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        userViewModel.getFriend(args.vkId)
    }

    private fun changeFavBtn() {
        val favRes = if (isFav) {
            R.drawable.ic_fav_filed
        } else R.drawable.ic_fav_border
        appBarChangesListener.onToolbarChanges(setAppBarConfig(favRes))
    }

    private fun setAppBarConfig(@DrawableRes favDrawableRes: Int) = AppBarConfig(
        firstButton = AppBarButton(favDrawableRes, ::onFavClick)
    )

    private fun onFavClick() {
        isFav = !isFav
        changeFavBtn()
        userViewModel.updateFavFriends(isFav)
    }

    private fun bindInfo(info: UserInfo) {
        with(binding) {
            setHasOptionsMenu(true)
            isFav = userViewModel.checkIsFav() == true
            changeFavBtn()
            appBarChangesListener.onTitleChanges(info.name)
            ivAvatar.load(info.photo){
                crossfade(true)
                placeholder(R.drawable.default_person)
            }
            ivAvatar.setOnClickListener {
                viewImage(
                    it,
                    info.photo,
                    getString(R.string.avatar_image, info.name)
                )
            }
            tvBirthdate.text = info.bdate
            tvInfo.text = info.about
            tvInfo.movementMethod = ScrollingMovementMethod()
            tvName.movementMethod = ScrollingMovementMethod()
            tvName.text = info.name
            progressBar.isVisible = false
            views.isVisible = true
        }
    }

    private fun initWishlists(wishlists: MutableList<Wishlist>) {
        val goToWishlist = { position: Int ->
            navigateToWishlist(position)
        }
        wishlistAdapter = WishlistAdapter(goToWishlist, null)
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
        binding.tvEmpty.isVisible = wishlists.isEmpty()
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

    private fun viewImage(transitionView: View, photo: String, title: String) {
        val action =
            UserFragmentDirections.actionUserFragmentToImageFragment(title, photo)
        findNavController().navigate(
            action, FragmentNavigator.Extras.Builder()
                .addSharedElements(
                    mapOf(transitionView to transitionView.transitionName)
                ).build()
        )
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
