package com.example.giftgiver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.giftgiver.R
import com.example.giftgiver.utils.FriendsListState
import com.example.giftgiver.utils.VKFriendsRequest
import com.example.giftgiver.user.User
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activityLauncher =
            registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
                when (result) {
                    is VKAuthenticationResult.Success -> {
                        // User passed authorization
                        val userId = VK.getUserId()
                        makeToast(userId.toString())
                        loadFriends()
                    }
                    is VKAuthenticationResult.Failed -> makeToast("Authentication error")
                }
            }
        activityLauncher.launch(arrayListOf(VKScope.FRIENDS))
    }

    private fun loadFriends() {
        VK.execute(
            VKFriendsRequest(),
            object : VKApiCallback<List<User>> {
                override fun success(result: List<User>) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToFirstFragment(
                            FriendsListState(result)
                        )
                    findNavController().navigate(action)
                }

                override fun fail(error: Exception) {
                    makeToast("Error while loading friends")
                }
            }
        )
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }
}
