package com.example.giftgiver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentListBinding
import com.example.giftgiver.utils.autoCleared
import com.example.giftgiver.user.User
import com.example.giftgiver.user.UserAdapter

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
            //todo add profile
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
}
