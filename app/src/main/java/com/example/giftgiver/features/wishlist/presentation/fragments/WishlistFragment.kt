package com.example.giftgiver.features.wishlist.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentWishlistBinding
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.AddGiftDialog
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.features.wishlist.domain.Wishlist
import com.example.giftgiver.features.wishlist.presentation.dialogs.ChangeWishlistDialog
import com.example.giftgiver.features.wishlist.presentation.viewModels.WishlistViewModel
import com.example.giftgiver.utils.SwipeToDeleteCallback
import com.example.giftgiver.utils.autoCleared
import java.io.File
import javax.inject.Inject

class WishlistFragment : Fragment() {
    lateinit var binding: FragmentWishlistBinding
    private val args: WishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private var index = 0
    private var isAdapterInited = false

    @Inject
    lateinit var clientStateRep: ClientStateRep

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var wishlistViewModel: WishlistViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        wishlistViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[WishlistViewModel::class.java]
        binding = FragmentWishlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        index = args.wishlistIndex
        wishlistViewModel.getWishlist(index)
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
        ChangeWishlistDialog().show(childFragmentManager, "dialog")
    }

    private fun bindInfo(wishlist: Wishlist) {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = wishlist.name
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
        giftAdapter = GiftAdapter(goToItem, gifts)
        with(binding.rvGifts) {
            adapter = giftAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)

            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    deleteGift(gifts[pos])
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

    private fun deleteGift(gift: Gift) {
        wishlistViewModel.deleteGift(gift)
    }

    private fun navigateToItem(giftId: String) {
        clientStateRep.getClient()?.vkId?.let {
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
        (activity as MainActivity).supportActionBar?.title = newName
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
