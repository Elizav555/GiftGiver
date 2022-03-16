package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.databinding.FragmentMyGiftBinding
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.utils.ClientState

class MyGiftFragment : Fragment() {
    private lateinit var binding: FragmentMyGiftBinding
    private val args: MyGiftFragmentArgs by navArgs()
    private val client = ClientState.client
    private var giftIndex = 0
    private var gifts = mutableListOf<Gift>()
    private val clients = ClientsRepositoryImpl()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyGiftBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }
    //todo edit gift and save changes

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
