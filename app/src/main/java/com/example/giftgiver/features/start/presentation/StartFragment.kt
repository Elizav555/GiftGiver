package com.example.giftgiver.features.start.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftgiver.App
import com.example.giftgiver.MainActivity
import com.example.giftgiver.common.viewModels.ViewModelFactory
import com.example.giftgiver.databinding.FragmentStartBinding
import com.example.giftgiver.features.calendar.domain.Calendar
import com.example.giftgiver.features.cart.domain.Cart
import com.example.giftgiver.features.client.domain.Client
import com.example.giftgiver.features.client.domain.repositories.ClientStateRep
import com.example.giftgiver.features.user.domain.useCases.LoadUserInfoVK
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    @Inject
    lateinit var clientStateRep: ClientStateRep

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var startViewModel: StartViewModel
    private val activityLauncher =
        registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    binding.btnLogin.isVisible = false
                    binding.progressBar.isVisible = true
                    makeToast("Welcome!")
                    startViewModel.getClient(VK.getUserId().value)
                }
                is VKAuthenticationResult.Failed -> makeToast("Authentication error")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        App.mainComponent.inject(this)
        startViewModel = ViewModelProvider(
            viewModelStore,
            viewModelFactory
        )[StartViewModel::class.java]
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
                        info = LoadUserInfoVK().loadInfo(vkId),
                        cart = Cart()
                    )
                    startViewModel.addClient(client)
                    clientStateRep.addClient(client)
                }
            } else clientStateRep.addClient(clientFromFB)
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

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }

    private fun initObservers() {
        startViewModel.client.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                getClientState(it)
                if (it != null) {
                    binding.btnLogin.isVisible = false
                    binding.progressBar.isVisible = true
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
