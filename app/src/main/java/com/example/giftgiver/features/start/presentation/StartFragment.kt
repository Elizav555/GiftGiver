package com.example.giftgiver.features.start.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentStartBinding
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVK
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.viewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartFragment : BaseFragment(R.layout.fragment_start) {
    private lateinit var binding: FragmentStartBinding

    @Inject
    lateinit var loadUserInfoVK: LoadUserInfoVK

    @Inject
    lateinit var clientStateRep: ClientStateRep
    private val startViewModel: StartViewModel by viewModel()
    private val activityLauncher =
        registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    binding.btnLogin.isVisible = false
                    binding.progressBar.isVisible = true
                    startViewModel.getClient(VK.getUserId().value)
                }
                is VKAuthenticationResult.Failed -> (activity as? MainActivity)?.makeToast("Authentication error")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        (activity as MainActivity).supportActionBar?.hide()
        initObservers()
        setHasOptionsMenu(false)
        initVK()
        binding.btnLogin.setOnClickListener {
            initVK()
        }
    }

    private fun getClientState(clientFromFB: Client?) {
        lifecycleScope.launch {
            if (clientFromFB == null) {
                val vkId = VK.getUserId().value
                lifecycleScope.launch {
                    val client = Client(
                        vkId,
                        Calendar(),
                        info = loadUserInfoVK.loadInfo(vkId),
                        cart = Cart()
                    )
                    startViewModel.addClient(client)
                }
            } else startViewModel.addClient(clientFromFB)
        }
    }

    private fun initVK() {
        if (VK.isLoggedIn()) {
            startViewModel.getClient(VK.getUserId().value)
        } else {
            binding.progressBar.isVisible = false
            binding.btnLogin.isVisible = true
            activityLauncher.launch(
                arrayListOf(
                    VKScope.FRIENDS,
                    VKScope.NOTIFICATIONS,
                    VKScope.OFFLINE
                )
            )
        }
    }

    private fun navigateToList() {
        lifecycleScope.launch {
            val action = StartFragmentDirections.actionStartFragmentToFriends()
            findNavController().navigate(action)
        }
    }

    private fun initObservers() {
        startViewModel.client.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                getClientState(it)
                if (it != null) {
                    binding.btnLogin.isVisible = false
                    binding.progressBar.isVisible = true
                    startViewModel.login()
                    startViewModel.loadFriends()
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
        startViewModel.friends.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                navigateToList()
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
