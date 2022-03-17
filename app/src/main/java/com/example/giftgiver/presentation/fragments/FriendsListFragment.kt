package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.data.mappers.FBMapper
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.domain.entities.User
import com.example.giftgiver.domain.usecase.LoadFriendsVK
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.user.UserAdapter
import com.example.giftgiver.utils.autoCleared
import com.vk.api.sdk.VK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FriendsListFragment : Fragment(R.layout.fragment_friends_list) {
    private lateinit var binding: FragmentFriendsListBinding
    private var userAdapter: UserAdapter by autoCleared()
    private lateinit var loadFriendsVK: LoadFriendsVK
    private val clients = ClientsRepositoryImpl(FBMapper())
    private var friends = listOf<User>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFriendsListBinding.inflate(inflater)
        initObjects()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        setHasOptionsMenu(true)
        loadFriends()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_filter, menu)
        val searchViewItem = menu.findItem(R.id.searchView)
        val searchView = searchViewItem.actionView as SearchView
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    val friendsName = friends.map { friend -> friend.name }
                    if (friendsName.contains(query)) {
                        userAdapter.filter.filter(query)
                    }
                    binding.recyclerView.scrollToPosition(0)
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    userAdapter.filter.filter(newText)
                    return false
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> {
                filterFriends()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterFriends() {
        //todo implement filter
    }

    private fun init(friends: List<User>) {
        val goToProfile = { position: Int ->
            navigateToProfile(friends[position].vkId)
        }
        userAdapter = UserAdapter(goToProfile, friends)
        with(binding.recyclerView) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
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

    private fun navigateToProfile(vkId: Long) {
        lifecycleScope.launch {
            val client = clients.getClientByVkId(vkId)
            client?.user = LoadUserInfoVK().loadInfo(vkId)
            client?.let {
                val action =
                    FriendsListFragmentDirections.actionFriendsListFragmentToUserFragment(
                        it
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun loadFriends() {
        lifecycleScope.launch {
            friends = loadFriendsVK.loadFriends(VK.getUserId().value)
            init(friends)
        }
    }

    private fun initObjects() {
        loadFriendsVK = LoadFriendsVK(
            clientsRep = ClientsRepositoryImpl(fbMapper = FBMapper()),
            dispatcher = Dispatchers.Default
        )
    }
}
