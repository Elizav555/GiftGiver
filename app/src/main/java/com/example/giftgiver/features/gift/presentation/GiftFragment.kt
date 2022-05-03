package com.example.giftgiver.features.gift.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.images.presentation.ImageViewModel
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.viewModel
import java.io.File

class GiftFragment : BaseFragment(R.layout.fragment_gift) {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private var curGift: Gift? = null
    private val imageViewModel: ImageViewModel by viewModel()
    private val giftViewModel: GiftViewModel by viewModel()
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
        initObservers()
        val vkId = args.userId
        giftViewModel.checkUser(vkId)
        giftViewModel.getGift(vkId, args.giftId)
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
            ivPhoto.setOnClickListener { gift.imageUrl?.let { url -> viewImage(url) } }
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

    private fun viewImage(photo: String) {
        val action =
            GiftFragmentDirections.actionGiftFragmentToImageFragment(photo)
        findNavController().navigate(action)
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
