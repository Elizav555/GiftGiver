package com.example.giftgiver.features.client.presentation

import android.app.AlertDialog
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.dialogs.AddWishlistDialog
import com.example.giftgiver.features.wishlist.presentation.list.WishlistAdapter
import com.example.giftgiver.utils.*
import java.io.File
import javax.inject.Inject

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private var wishlistAdapter: WishlistAdapter by autoCleared()
    private lateinit var binding: FragmentAccountBinding
    private var isAdapterInited = false
    private val clientViewModel: ClientViewModel by viewModel()

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
        checkTheme()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        clientViewModel.getInfo()
        clientViewModel.getWishlists()
        bindAll()
    }

    private fun enterEditMode() {
        clientViewModel.getClient()
            ?.let { EditClientDialog(it).show(childFragmentManager, "dialog") }
    }

    private fun changeTheme() {
        ThemeUtils.isLight = !ThemeUtils.isLight
        checkTheme()
    }

    private fun checkTheme() {
        val themeRes = if (ThemeUtils.isLight) {
            R.drawable.ic_baseline_dark_mode_24
        } else {
            R.drawable.ic_baseline_light_mode_24
        }
        appBarChangesListener.onToolbarChanges(setAppBarConfig(themeRes))
    }

    private fun setAppBarConfig(@DrawableRes themeDrawableRes: Int) = AppBarConfig(
        firstButton = AppBarButton(R.drawable.ic_baseline_edit_24, ::enterEditMode),
        secondButton = AppBarButton(
            themeDrawableRes,
            ::changeTheme
        ),
        title = "Account"
    )

    private fun bindAll() {
        with(binding) {
            btnLogout.setOnClickListener {
                logout()
            }
            btnAdd.setOnClickListener {
                AddWishlistDialog()
                    .show(childFragmentManager, "dialog")
            }
            tvInfo.movementMethod = ScrollingMovementMethod()
            tvName.movementMethod = ScrollingMovementMethod()
            progressBar.isVisible = false
            views.isVisible = true
        }
    }

    private fun logout(){
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.dialog_logout))
                .setPositiveButton(R.string.logout) { _, _ ->
                    clientViewModel.logout()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }
                .create()
            dialog.show()
        }
    }

    private fun viewImage(transitionView: View, photo: String, title: String) {
        isAdapterInited = false
        val action =
            AccountFragmentDirections.actionAccountToImageFragment(title, photo)
        findNavController().navigate(
            action, FragmentNavigator.Extras.Builder()
                .addSharedElements(
                    mapOf(transitionView to transitionView.transitionName)
                ).build()
        )
    }

    private fun bindInfo(userInfo: UserInfo) =
        with(userInfo) {
            binding.ivAvatar.setOnClickListener {
                viewImage(it, photo, getString(R.string.avatar_image, name))
            }
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
        wishlistAdapter = WishlistAdapter(goToWishlist, delete)
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
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.delete_wishlist))
                .setMessage(getString(R.string.action_cannot_undone))
                .setPositiveButton(R.string.delete) { _, _ ->
                    clientViewModel.deleteWishlist(wishlist)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }
                .create()
            dialog.show()
        }
    }

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
                binding.tvEmpty.isVisible = it.isEmpty()
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
