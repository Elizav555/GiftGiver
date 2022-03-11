package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.usecase.LoadFriendsVK
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.user.UserAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
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
        binding.toolbar.inflateMenu(R.menu.menu_filter)
        initVK()
    }

    private fun init(friends: List<User>) {
        val goToProfile = { position: Int ->
            LoadUserInfoVK().loadInfo(friends[position].vkId, ::navigateToProfile)
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

    private fun navigateToProfile(user: User) {
        val action =
            FriendsListFragmentDirections.actionFriendsListFragmentToUserFragment(
                user
            )
        findNavController().navigate(action)
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

    private fun loadFriends() = LoadFriendsVK().loadFriends(VK.getUserId().value, ::init)

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }
}
