package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftgiver.databinding.FragmentStartBinding
import com.example.giftgiver.domain.entities.Calendar
import com.example.giftgiver.domain.entities.Cart
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.usecase.AddClientUseCase
import com.example.giftgiver.domain.usecase.GetClientByVkId
import com.example.giftgiver.domain.usecase.LoadFriendsVK
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.App
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding

    @Inject
    lateinit var getClientByVkId: GetClientByVkId

    @Inject
    lateinit var loadFriendsVK: LoadFriendsVK

    @Inject
    lateinit var addClient: AddClientUseCase
    private val activityLauncher =
        registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    makeToast("Welcome!")
                    getClientState()
                    navigateToList()
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
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        (activity as MainActivity).supportActionBar?.hide()
        setHasOptionsMenu(false)
        initVK()
        binding.btnLogin.setOnClickListener {
            initVK()
        }
    }

    private fun getClientState() {
        val vkId = VK.getUserId().value
        lifecycleScope.launch {
            val clientFromFB = checkClient(vkId)
            if (clientFromFB == null) {
                lifecycleScope.launch {
                    val client = Client(
                        vkId,
                        Calendar(),
                        info = LoadUserInfoVK().loadInfo(vkId),
                        cart = Cart()
                    )
                    addClient(client)
                    ClientState.client = client
                }
            } else lifecycleScope.launch {
                ClientState.client = clientFromFB
            }
        }
    }

    private suspend fun checkClient(vkId: Long) =
        getClientByVkId(vkId)

    private fun initVK() {
        if (VK.isLoggedIn()) {
            getClientState()
            navigateToList()
        } else {
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
            binding.btnLogin.isVisible = false
            binding.progressBar.isVisible = true
            FriendsState.friends = loadFriendsVK.loadFriends(VK.getUserId().value)
            val action = StartFragmentDirections.actionStartFragmentToFriends()
            findNavController().navigate(action)
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }
}
