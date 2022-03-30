package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.presentation.App
import com.example.giftgiver.presentation.MainActivity
import com.example.giftgiver.presentation.user.UserAdapter
import com.example.giftgiver.utils.ClientState
import com.example.giftgiver.utils.FriendsState
import com.example.giftgiver.utils.autoCleared
import kotlinx.coroutines.launch

class FriendsListFragment : Fragment(R.layout.fragment_friends_list) {
    private lateinit var binding: FragmentFriendsListBinding
    private var userAdapter: UserAdapter by autoCleared()
    private var friends = FriendsState.friends
    private var isFav = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        App.mainComponent.inject(this)
        binding = FragmentFriendsListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        init()
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
                filterFriends(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterFriends(item: MenuItem) {
        isFav = !isFav
        if (isFav) {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_filed,
                null
            )
            val client = ClientState.client
            client?.let {
                friends =
                    it.favFriends.map { friend -> friend.info }.sortedBy { friend -> friend.name }
            }
            updateList()
        } else {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_border,
                null
            )
            lifecycleScope.launch {
                friends = FriendsState.friends
                updateList()
            }
        }
    }

    private fun init() {
        val goToProfile = { id: Long ->
            navigateToProfile(id)
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
        updateList()
    }

    private fun navigateToProfile(vkId: Long) {
        val action = FriendsListFragmentDirections.actionFriendsListFragmentToUserFragment(vkId)
        findNavController().navigate(action)
    }

    private fun updateList() {
        binding.tvEmpty.isVisible = friends.isEmpty()
        userAdapter.submitList(friends)
    }
}
