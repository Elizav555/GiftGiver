package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentMyGiftBinding
import com.example.giftgiver.domain.entities.Client
import com.google.firebase.firestore.ktx.toObject
import com.vk.api.sdk.VK
import kotlinx.coroutines.launch

class MyGiftFragment : Fragment() {
    private lateinit var binding: FragmentMyGiftBinding
    private val args: MyGiftFragmentArgs by navArgs()
    private lateinit var client: Client
    private var giftIndex = 0
    private var wishlistIndex = 0
    private val clients = ClientsRepositoryImpl()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyGiftBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            clients.getClientByVkId(VK.getUserId().value).addOnSuccessListener {
                it.toObject<ClientFB>()?.let { clientFB ->
                    lifecycleScope.launch { bindInfo(FBMapper().mapClientFromFB(clientFB)) }
                }
            }
        }
    }

    private fun bindInfo(clientFB: Client) {
        client = clientFB
        giftIndex = args.giftIndex
        wishlistIndex = args.wishlistIndex
        val gift = client.user.info.wishlists[wishlistIndex].gifts[giftIndex]
        with(binding) {
            toolbar.inflateMenu(R.menu.menu_edit)
            toolbar.title = gift.name
            tvDesc.text = gift.desc
            ivPhoto.load(gift.imageUrl)
        }
    }
}
