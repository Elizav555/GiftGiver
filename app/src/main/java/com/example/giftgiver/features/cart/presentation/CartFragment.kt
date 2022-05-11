package com.example.giftgiver.features.cart.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentCartBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.forCart.GiftCartAdapter
import com.example.giftgiver.utils.*

class CartFragment : BaseFragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private var giftAdapter: GiftCartAdapter by autoCleared()
    private val cartViewModel: CartViewModel by viewModel()
    private var giftsForList: List<Gift>? = null
    private var isAdapterInited = false
    var appBarChangesListener: OnAppBarChangesListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appBarChangesListener = context as? OnAppBarChangesListener
        appBarChangesListener?.onToolbarChanges(
            AppBarConfig(
                firstButton = AppBarButton(
                    R.drawable.ic_baseline_delete_outline_24,
                    ::showDeleteDialog
                ),
                title = "Cart"
            )
        )
        initObservers()
        cartViewModel.getGifts()
    }

    private fun deleteAll() = cartViewModel.deleteAll()

    private fun initAdapter(gifts: List<Gift>) {
        val goToItem = { id: String ->
            navigateToItem(giftsForList?.first { it.id == id })
        }
        giftAdapter = GiftCartAdapter(goToItem, cartViewModel.getGiftsInfo())
        with(binding.rvCart) {
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
                    deleteGift(cartViewModel.getGiftByPos(pos), pos)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    cartViewModel.moveGift(
                        viewHolder.adapterPosition,
                        target.adapterPosition,
                    )
                    return true
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
        giftAdapter.submitList(gifts)
        binding.progressBar.isVisible = false
    }

    private fun deleteGift(gift: Gift, pos: Int) {
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.deleteGiftTitle))
                .setMessage(getString(R.string.deleteGiftMessage))
                .setPositiveButton(R.string.delete) { _, _ ->
                    cartViewModel.delete(gift)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                    giftAdapter.notifyItemChanged(pos)
                }
                .create()
            dialog.show()
        }
    }

    private fun navigateToItem(gift: Gift?) {
        cartViewModel.updateClient()
        gift?.let {
            isAdapterInited = false
            val action = CartFragmentDirections.actionCartToCartGiftFragment(
                it.id,
                it.forId
            )
            findNavController().navigate(action)
        }
    }

    private fun initObservers() {
        cartViewModel.gifts.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                giftsForList = it
                if (isAdapterInited) {
                    giftAdapter.submitList(it)
                } else {
                    isAdapterInited = true
                    initAdapter(it)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }

    private fun showDeleteDialog() {
        activity?.let {
            val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme)
                .setTitle(getString(R.string.deleteGiftsTitle))
                .setMessage(getString(R.string.deleteGiftsMessage))
                .setPositiveButton(R.string.delete_all) { _, _ ->
                    deleteAll()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog?.cancel()
                }
                .create()
            dialog.show()
        }
    }
}
