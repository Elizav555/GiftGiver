package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.databinding.FragmentGiftBinding
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.utils.ClientState

class GiftFragment : Fragment() {
    private lateinit var binding: FragmentGiftBinding
    private val args: GiftFragmentArgs by navArgs()
    private val client = ClientState.client
    private var giftIndex = 0
    private var gifts = mutableListOf<Gift>()
    private val clients = ClientsRepositoryImpl()
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
        bindInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }
    //todo edit gift if it's client's gift and save changes

    private fun bindInfo() {
        giftIndex = args.giftIndex
        gifts = args.gifts.toMutableList()
        val gift = gifts[giftIndex]
        with(binding) {
            setHasOptionsMenu(true)
            (activity as MainActivity).supportActionBar?.title = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl)
        }
    }
}
