package com.example.giftgiver.features.cart.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentCartBinding
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.forCart.GiftCartAdapter
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.SwipeToDeleteCallback
import com.example.giftgiver.utils.autoCleared
import com.example.giftgiver.utils.viewModel
import javax.inject.Inject

class CartFragment : BaseFragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private var giftAdapter: GiftCartAdapter by autoCleared()

    @Inject
    lateinit var clientStateRep: ClientStateRep
    private val cartViewModel: CartViewModel by viewModel()
    private var giftsForList: List<Gift>? = null
    private var isAdapterInited = false
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
        setHasOptionsMenu(true)
        initObservers()
        cartViewModel.getGifts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                showDeleteDialog(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAll() = cartViewModel.deleteAll()

    private fun initAdapter(gifts: List<Gift>) {
        val goToItem = { id: String ->
            navigateToItem(giftsForList?.first { it.id == id })
        }
        giftAdapter = GiftCartAdapter(goToItem, clientStateRep.getClient()?.cart?.giftsInfo)
        with(binding.rvCart) {
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
                    deleteGift(gifts[pos], pos)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
        giftAdapter.submitList(gifts)
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
                gift.id,
                gift.forId
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

    private fun showDeleteDialog(item: MenuItem) {
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
