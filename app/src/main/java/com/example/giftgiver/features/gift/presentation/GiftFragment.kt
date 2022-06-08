package com.example.giftgiver.features.gift.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.features.gift.domain.Gift
import com.example.giftgiver.features.images.presentation.ImageViewModel
import com.example.giftgiver.utils.*
import java.io.File
import javax.inject.Inject

class GiftFragment : BaseFragment(R.layout.fragment_gift) {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private var curGift: Gift? = null
    private val imageViewModel: ImageViewModel by viewModel()
    private val giftViewModel: GiftViewModel by viewModel()

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftBinding.inflate(inflater)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        val vkId = args.userId
        giftViewModel.checkUser(vkId)
        giftViewModel.getGift(vkId, args.giftId)
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
            tvForName.movementMethod = ScrollingMovementMethod()
            appBarChangesListener.onTitleChanges(gift.name)
            tvDesc.text = gift.desc
            ivPhoto.setOnClickListener {
                gift.imageUrl?.let { url ->
                    viewImage(
                        ivPhoto,
                        url,
                        getString(R.string.gift_image, gift.name)
                    )
                }
            }
            ivPhoto.load(gift.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.default_gift)
            }
            imageViewModel.imageBitmapLiveData.observe(viewLifecycleOwner) {
                ivPhoto.setImageBitmap(it)
            }
            progressBar.isVisible = false
            groupAll.isVisible = true
        }
    }

    private fun changeGift(newName: String, newDesc: String, newImageFile: File?) {
        binding.progressBar.isVisible = true
        binding.groupAll.isVisible = false
        giftViewModel.changeGift(newName, newDesc, newImageFile)
    }

    private fun viewImage(transitionView: View, photo: String, title: String) {
        val action =
            GiftFragmentDirections.actionGiftFragmentToImageFragment(title, photo)
        findNavController().navigate(
            action, FragmentNavigator.Extras.Builder()
                .addSharedElements(
                    mapOf(transitionView to transitionView.transitionName)
                ).build()
        )
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
                appBarChangesListener.onToolbarChanges(
                    AppBarConfig(
                        firstButton = if (isClient) AppBarButton(
                            R.drawable.ic_baseline_edit_24,
                            ::enterEditMode
                        ) else null,
                    )
                )
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
