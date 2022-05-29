package com.example.giftgiver.features.images.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import coil.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentImageBinding
import com.example.giftgiver.utils.AppBarConfig
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.OnAppBarChangesListener
import javax.inject.Inject

class ImageFragment : BaseFragment(R.layout.fragment_image) {
    private lateinit var binding: FragmentImageBinding
    private val args: ImageFragmentArgs by navArgs()

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener
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
        val link = args.imageURL
        with(binding) {
            imageView.load(link)
        }
        appBarChangesListener.onToolbarChanges(
            AppBarConfig(
                title = args.title
            )
        )
        return binding.root
    }
}