package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.usecase.LoadFriendsVK
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.user.UserAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK

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
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        binding.toolbar.inflateMenu(R.menu.menu_filter)
        loadFriends()
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

    private fun loadFriends() = LoadFriendsVK().loadFriends(VK.getUserId().value, ::init)
}
