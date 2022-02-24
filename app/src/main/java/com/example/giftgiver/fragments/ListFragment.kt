package com.example.giftgiver.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentListBinding
import com.example.giftgiver.user.User
import com.example.giftgiver.user.UserAdapter
import com.example.giftgiver.user.UserInfo
import com.example.giftgiver.utils.VKUserInfoRequest
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private var userAdapter: UserAdapter by autoCleared()
    private val args: ListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val friends = args.friendsListState.friendsList
        init(friends)
        userAdapter.submitList(friends)
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
    }

    private fun loadInfo(user: User) {
        VK.execute(
            VKUserInfoRequest(user.id),
            object : VKApiCallback<UserInfo> {
                override fun success(result: UserInfo) {
                    user.info = result
                    val action =
                        ListFragmentDirections.actionListFragmentToUserFragment(
                            user
                        )
                    findNavController().navigate(action)
                }

                override fun fail(error: Exception) {
                    makeToast("Error while loading user info ")
                    Log.println(Log.ERROR, "", error.toString())
                }
            }
        )
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG)
            .show()
    }
}
