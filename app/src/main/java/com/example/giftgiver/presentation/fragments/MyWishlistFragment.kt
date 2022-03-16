package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.databinding.FragmentMyWishlistBinding
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.presentation.gift.GiftAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.SwipeToDeleteCallback
import com.example.giftgiver.utils.autoCleared
import kotlinx.coroutines.launch

class MyWishlistFragment : Fragment() {
    lateinit var binding: FragmentMyWishlistBinding
    private val args: MyWishlistFragmentArgs by navArgs()
    private var giftAdapter: GiftAdapter by autoCleared()
    private val client = ClientState.client
    private var index = 0
    private val clients = ClientsRepositoryImpl()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyWishlistBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInfo()
    }

    private fun bindInfo() {
        client?.let {
            index = args.wishlistIndex
            val wishlist = client.user.info.wishlists[index]
            binding.toolbar.title = wishlist.name
            binding.toolbar.inflateMenu(R.menu.menu_edit)
            binding.addItem.setOnClickListener {
                AddGiftDialogFragment()
                    .show(childFragmentManager, "dialog")
            }
            initAdapter(wishlist.gifts)
        }
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { position: Int ->
            navigateToItem(position)
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

    fun addGift(gift: Gift) = client?.let {
        client.user.info.wishlists[index].gifts.add(gift)
        lifecycleScope.launch {
            clients.updateClient(client.vkId, mapOf("wishlists" to client.user.info.wishlists))
        }
        giftAdapter.submitList(client.user.info.wishlists[index].gifts)
    }

    private fun deleteGift(gift: Gift) = client?.let {
        client.user.info.wishlists[index].gifts.remove(gift)
        lifecycleScope.launch {
            clients.updateClient(client.vkId, mapOf("wishlists" to client.user.info.wishlists))
        }
        giftAdapter.submitList(client.user.info.wishlists[index].gifts)
    }

    private fun navigateToItem(giftIndex: Int) {
        client?.let {
            val action =
                MyWishlistFragmentDirections.actionMyWishlistFragmentToGiftFragment(
                    giftIndex,
                    client.user.info.wishlists[index].gifts.toTypedArray()
                )
            findNavController().navigate(action)
        }
    }
}
