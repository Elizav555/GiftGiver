package com.example.giftgiver.features.gift.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.common.db.fileStorage.ImageStorageImpl
import com.example.giftgiver.common.viewModels.ImageViewModel
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.wishlist.domain.UpdateWishlistUseCase
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class GiftFragment : Fragment() {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private val client = ClientState.client
    private var giftIndex = 0
    private var gifts = mutableListOf<Gift>()
    private var isClient = false

    @Inject
    lateinit var updateWishlists: UpdateWishlistUseCase

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var imageViewModel: ImageViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        App.mainComponent.inject(this)
        imageViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[ImageViewModel::class.java]
        binding = FragmentGiftBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isClient = args.isClient
        giftIndex = args.giftIndex
        gifts = args.gifts.toMutableList()
        val gift = gifts[giftIndex]
        bindInfo(gift)
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
        val submitAction = { newName: String, newDesc: String, newFile: File? ->
            changeGift(
                newName,
                newDesc,
                newFile
            )
        }
        AddGiftDialog(submitAction)
            .show(childFragmentManager, "dialog")
    }

    private fun bindInfo(gift: Gift) {
        with(binding) {
            groupFor.isVisible = !isClient
            tvForName.text = gift.forName
            tvDesc.movementMethod = ScrollingMovementMethod()
            setHasOptionsMenu(isClient)
            (activity as MainActivity).supportActionBar?.title = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl)
            imageViewModel.imageBitmapLiveData.observe(viewLifecycleOwner) {
                ivPhoto.setImageBitmap(it)
            }
        }
    }

    private fun changeGift(newName: String, newDesc: String, newImageFile: File?) = client?.let {
        val index = args.wishlistIndex
        if (!isClient) {
            return@let
        }
        lifecycleScope.launch {
            val gift = client.wishlists[index].gifts[giftIndex]
            newImageFile?.let {
                gift.imageUrl = ImageStorageImpl().addImage(newImageFile).toString()
            }
            client.wishlists[index].gifts[giftIndex] =
                Gift(newName, gift.forId, gift.forName, newDesc, gift.imageUrl, gift.isChosen)
            updateWishlists(client.vkId, client.wishlists)
            bindInfo(client.wishlists[index].gifts[giftIndex])
        }
    }
}
