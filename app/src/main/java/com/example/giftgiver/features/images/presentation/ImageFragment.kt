package com.example.giftgiver.features.images.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentImageBinding
import com.example.giftgiver.utils.AppBarConfig
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.OnAppBarChangesListener

class ImageFragment : BaseFragment(R.layout.fragment_image) {
    private lateinit var binding: FragmentImageBinding
    private val args: ImageFragmentArgs by navArgs()
    var appBarChangesListener: OnAppBarChangesListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater)
        val transition =
            TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        var link = args.imageURL
        with(binding) {
            imageView.load(link)
        }
        appBarChangesListener = context as? OnAppBarChangesListener
        appBarChangesListener?.onToolbarChanges(
            AppBarConfig(
                title = args.title
            )
        )
        return binding.root
    }
}