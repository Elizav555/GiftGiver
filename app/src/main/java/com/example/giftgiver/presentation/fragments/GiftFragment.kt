package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.ImageStorage
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.presentation.ImageViewModel
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.utils.ClientState
import kotlinx.coroutines.launch
import java.io.File

class GiftFragment : Fragment() {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private val client = ClientState.client
    private var giftIndex = 0
    private var gifts = mutableListOf<Gift>()
    private var isClient = false
    private val clients = ClientsRepositoryImpl(fbMapper = FBMapper())
    private val viewModel by viewModels<ImageViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            R.id.add -> {
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
            setHasOptionsMenu(isClient)
            (activity as MainActivity).supportActionBar?.title = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl)
            viewModel.imageBitmapLiveData.observe(viewLifecycleOwner) {
                ivPhoto.setImageBitmap(it)
            }
        }
    }

    private fun changeGift(newName: String, newDesc: String, newImageFile: File?) = client?.let {
        val index = args.wishlistIndex
        if (!isClient)
            return@let
        lifecycleScope.launch {
            val gift = client.wishlists[index].gifts[giftIndex]
            newImageFile?.let {
                gift.imageUrl = ImageStorage().addGiftImage(newImageFile).toString()
            }
            client.wishlists[index].gifts[giftIndex] =
                Gift(newName, gift.forId, gift.forName, newDesc, gift.imageUrl, gift.isChosen)
            clients.updateWishlists(client.vkId, client.wishlists)
            bindInfo(client.wishlists[index].gifts[giftIndex])
        }
    }
}
