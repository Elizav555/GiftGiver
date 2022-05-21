package com.example.giftgiver.features.wishlist.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentWishlistBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.AddGiftDialog
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.dialogs.ChangeWishlistDialog
import com.example.giftgiver.features.wishlist.presentation.viewModels.WishlistViewModel
import com.example.giftgiver.utils.*
import java.io.File
import javax.inject.Inject

class WishlistFragment : BaseFragment(R.layout.fragment_wishlist) {
    lateinit var binding: FragmentWishlistBinding
    private val args: WishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var index = 0

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener
    private var isAdapterInited = false
    private val wishlistViewModel: WishlistViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        appBarChangesListener.onToolbarChanges(
            AppBarConfig(
                firstButton = AppBarButton(R.drawable.ic_baseline_edit_24, ::enterEditMode),
                title = "Wishlist"
            )
        )
        index = args.wishlistIndex
        wishlistViewModel.getWishlist(index)
    }

    private fun enterEditMode() {
        ChangeWishlistDialog().show(childFragmentManager, "dialog")
    }

    private fun bindInfo(wishlist: Wishlist) {
        setHasOptionsMenu(true)
        appBarChangesListener.onTitleChanges(wishlist.name)
        binding.addItem.setOnClickListener {
            val submitAction = { newName: String, newDesc: String, newFile: File? ->
                addGift(
                    newName,
                    newDesc,
                    newFile
                )
            }
            AddGiftDialog(submitAction)
                .show(childFragmentManager, "dialog")
        }
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { id: String ->
            navigateToItem(id)
        }
        giftAdapter = GiftAdapter(goToItem)
        with(binding.rvGifts) {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)

            val swipeToDeleteCallback = object : MySwipeCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    wishlistViewModel.getGiftByPos(pos)?.let { deleteGift(it, pos) }
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    wishlistViewModel.moveGift(
                        viewHolder.adapterPosition,
                        target.adapterPosition
                    )
                    return true
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
        giftAdapter.submitList(gifts)
    }

    private fun addGift(newName: String, newDesc: String, newImageFile: File?) {
        wishlistViewModel.addGift(newName, newDesc, newImageFile)
    }

    private fun deleteGift(gift: Gift, pos: Int) {
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.delete_gift))
                .setMessage(getString(R.string.action_cannot_undone))
                .setPositiveButton(R.string.delete) { _, _ ->
                    wishlistViewModel.deleteGift(gift)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                    giftAdapter.notifyItemChanged(pos)
                }
                .create()
            dialog.show()
        }
    }

    private fun navigateToItem(giftId: String) {
        wishlistViewModel.getClientId()?.let {
            isAdapterInited = false
            val action =
                WishlistFragmentDirections.actionMyWishlistFragmentToGiftFragment(
                    it,
                    giftId
                )
            findNavController().navigate(action)
        }
    }

    fun changeWishlistName(newName: String) {
        wishlistViewModel.changeWishlistName(newName)
        appBarChangesListener.onTitleChanges(newName)
    }

    private fun initObservers() {
        wishlistViewModel.wishlist.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { wishlist ->
                wishlist?.let {
                    bindInfo(it)
                    wishlistViewModel.getGifts(it.giftsIds)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
        wishlistViewModel.gifts.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                if (isAdapterInited) {
                    giftAdapter.submitList(it)
                } else {
                    isAdapterInited = true
                    initAdapter(it as MutableList<Gift>)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
