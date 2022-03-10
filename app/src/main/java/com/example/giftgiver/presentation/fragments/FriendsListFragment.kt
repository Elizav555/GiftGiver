package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.data.vk.VKFriendsRequest
import com.example.giftgiver.data.vk.VKUserWithInfoRequest
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.presentation.user.UserAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class FriendsListFragment : Fragment(R.layout.fragment_friends_list) {
    private lateinit var binding: FragmentFriendsListBinding
    private var userAdapter: UserAdapter by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFriendsListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVK()
    }

    private fun init(friends: List<User>) {
        val goToProfile = { position: Int ->
            loadInfo(friends[position])
        }
        userAdapter = UserAdapter(goToProfile, friends)
        with(binding.recyclerView) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            val dividerItemDecoration = DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }
        binding.progressBar.visibility = View.GONE
        userAdapter.submitList(friends)
    }

    private fun loadInfo(user: User) {
        VK.execute(
            VKUserWithInfoRequest(user.vkId),
            object : VKApiCallback<User> {
                override fun success(result: User) {
//                    user = result
                    val action =
                        FriendsListFragmentDirections.actionFriendsListFragmentToUserFragment(
                            result
                        )
                    findNavController().navigate(action)
                }

                override fun fail(error: Exception) {
                    makeToast("Error while loading user infoVk ")
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }

    private fun initVK() {
        if (VK.isLoggedIn()) {
            loadFriends()
        } else {
            val activityLauncher =
                registerForActivityResult(VK.getVKAuthActivityResultContract()) { result ->
                    when (result) {
                        is VKAuthenticationResult.Success -> {
                            val userId = VK.getUserId()
                            makeToast(userId.toString())
                            loadFriends()
                        }
                        is VKAuthenticationResult.Failed -> makeToast("Authentication error")
                    }
                }
            activityLauncher.launch(
                arrayListOf(
                    VKScope.FRIENDS,
                    VKScope.NOTIFICATIONS,
                    VKScope.OFFLINE
                )
            )
        }
    }

    private fun loadFriends() {
        VK.execute(
            VKFriendsRequest(VK.getUserId().value),
            object : VKApiCallback<List<User>> {
                override fun success(result: List<User>) {
                    init(result)
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
