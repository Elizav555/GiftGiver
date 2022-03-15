package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentStartBinding
import com.example.giftgiver.domain.entities.Calendar
import com.example.giftgiver.domain.entities.Cart
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.utils.ClientState
import com.google.firebase.firestore.ktx.toObject
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.*

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private val clientsRep = ClientsRepositoryImpl()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
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
        binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.GONE)
        initVK()
        binding.btnLogin.setOnClickListener {
            initVK()
        }
    }

    private fun getClientState() {
        val vkId = VK.getUserId().value
        lifecycleScope.launch {
            checkClient(vkId).addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.data == null) {
                    lifecycleScope.launch {
                        val client = Client(
                            vkId,
                            Calendar(),
                            LoadUserInfoVK().loadInfo(vkId),
                            Cart()
                        )
                        clientsRep.addClient(
                            client
                        )
                        setClientState(client)
                    }
                } else lifecycleScope.launch {
                    clientsRep.getClientByVkId(VK.getUserId().value).addOnSuccessListener {
                        it.toObject<ClientFB>()?.let { client ->
                            lifecycleScope.launch { setClientState(FBMapper().mapClientFromFB(client)) }
                        }
                    }
                }
            }
        }
    }

    private fun setClientState(client: Client) {
        ClientState.client = client
    }

    private suspend fun checkClient(vkId: Long) =
        withContext(scope.coroutineContext) {
            clientsRep.getClientByVkId(vkId)
        }

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
        val action = StartFragmentDirections.actionStartFragmentToFriends()
        findNavController().navigate(action)
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }
}
