package com.example.giftgiver.features.cart.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftgiver.App
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentCartBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.gift.presentation.list.GiftAdapter
import com.example.giftgiver.utils.SwipeToDeleteCallback
import com.example.giftgiver.utils.autoCleared
import javax.inject.Inject

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private var giftAdapter: GiftAdapter by autoCleared()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var cartViewModel: CartViewModel
    private var isAdapterInited = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        cartViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[CartViewModel::class.java]
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
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAll() = cartViewModel.deleteAll()

    private fun initAdapter(gifts: List<Gift>) {
        val goToItem = { position: Int -> //todo change from pos
            navigateToItem(position, gifts)
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

    private fun deleteGift(gift: Gift) = cartViewModel.delete(gift)

    private fun navigateToItem(giftIndex: Int, gifts: List<Gift>) {
        isAdapterInited = false
        val action = CartFragmentDirections.actionCartToCartGiftFragment(
            giftIndex,
            gifts.toTypedArray(),//todo change to id
            false,
            -1
        )
        findNavController().navigate(action)
    }

    private fun initObservers() {
        cartViewModel.gifts.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val gifts = it
                if (isAdapterInited) {
                    giftAdapter.submitList(gifts)
                } else {
                    isAdapterInited = true
                    initAdapter(gifts)
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
