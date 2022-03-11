package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.api.load
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentAccountBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.vk.api.sdk.VK

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoadUserInfoVK().loadInfo(VK.getUserId().value, ::bindInfo)
    }

    private fun bindInfo(client: User) {
        with(binding) {
            toolbar.inflateMenu(R.menu.menu_edit)
            btnLogout.setOnClickListener {
                VK.logout()
            }
            ivAvatar.load(client.info?.photoMax)
            tvBirthdate.text = client.info?.bdate
            tvInfo.text = client.info?.about
            tvName.text = client.name
            progressBar.visibility = View.GONE
            views.visibility = View.VISIBLE
        }
    }
}
