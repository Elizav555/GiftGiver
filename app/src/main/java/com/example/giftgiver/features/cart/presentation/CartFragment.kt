package com.example.giftgiver.features.cart.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentCartBinding
import com.example.giftgiver.features.cart.domain.UpdateCartUseCase
import com.example.giftgiver.features.client.domain.useCases.GetClientByVkId
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.SwipeToDeleteCallback
import com.example.giftgiver.utils.autoCleared
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val client = ClientState.client
    private var giftAdapter: GiftAdapter by autoCleared()

    @Inject
    lateinit var updateCart: UpdateCartUseCase

    @Inject
    lateinit var getClientByVkId: GetClientByVkId
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        client?.let { initAdapter(client.cart.gifts) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAll() = client?.let {
        client.cart.gifts.clear()
        lifecycleScope.launch {
            updateCart(client.vkId, client.cart.gifts)
        }
        giftAdapter.submitList(client.cart.gifts)
    }

    private fun initAdapter(gifts: MutableList<Gift>) {
        val goToItem = { position: Int ->
            navigateToItem(position)
        }
        giftAdapter = GiftAdapter(goToItem, gifts, null, true)
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
                    deleteGift(gifts[pos])
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
        giftAdapter.submitList(gifts)
    }

    private fun getClientByVkIdAsync(vkId: Long) =
        lifecycleScope.async { getClientByVkId(vkId) }

    private fun deleteGift(gift: Gift) = client?.let {
        client.cart.gifts.remove(gift)
        lifecycleScope.launch {
            updateCart(client.vkId, client.cart.gifts)
        }
        giftAdapter.submitList(client.cart.gifts)
    }

    private fun navigateToItem(giftIndex: Int) {
        client?.let {
            val action = CartFragmentDirections.actionCartToCartGiftFragment(
                giftIndex,
                client.cart.gifts.toTypedArray(),
                false,
                -1
            )
            findNavController().navigate(action)
        }
    }
}
