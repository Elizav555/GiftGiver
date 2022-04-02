package com.example.giftgiver.features.gift.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.common.viewModels.ImageViewModel
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import java.io.File
import javax.inject.Inject

class GiftFragment : Fragment() {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private var giftIndex = 0
    private var wishlistIndex = 0
    private var curGift: Gift? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var giftViewModel: GiftViewModel
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
        giftViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[GiftViewModel::class.java]
        binding = FragmentGiftBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        wishlistIndex = args.wishlistIndex
        giftIndex = args.giftIndex
        val vkId = args.userId
        giftViewModel.checkUser(vkId)
        giftViewModel.getGift(vkId, wishlistIndex, giftIndex)
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
        AddGiftDialog(submitAction, curGift)
            .show(childFragmentManager, "dialog")
    }

    private fun bindInfo(gift: Gift) {
        with(binding) {
            tvForName.text = gift.forName
            tvDesc.movementMethod = ScrollingMovementMethod()
            (activity as MainActivity).supportActionBar?.title = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl)
            imageViewModel.imageBitmapLiveData.observe(viewLifecycleOwner) {
                ivPhoto.setImageBitmap(it)
            }
            progressBar.isVisible = false
            groupAll.isVisible = true
        }
    }

    private fun changeGift(newName: String, newDesc: String, newImageFile: File?) {
        giftViewModel.changeGift(newName, newDesc, newImageFile)
    }

    private fun initObservers() {
        giftViewModel.gift.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { gift ->
                curGift = gift
                gift?.let { bindInfo(it) }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
        giftViewModel.isClient.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val isClient = it
                binding.groupFor.isVisible = !isClient
                setHasOptionsMenu(isClient)
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
