package com.example.giftgiver.features.images.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentImageBinding
import com.example.giftgiver.utils.BaseFragment

class ImageFragment : BaseFragment(R.layout.fragment_image) {
    private lateinit var binding: FragmentImageBinding
    private val args: ImageFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var link = args.imageURL
        with(binding) {
            imageView.load(link) {
                listener(
                    onSuccess = { _, _ ->
                        progressBar.isVisible = false
                        imageView.isVisible = true
                    },
                    onError = { _, throwable: Throwable ->
                        Log.e("coil", throwable.message.toString())
                    })
            }
        }
    }
}