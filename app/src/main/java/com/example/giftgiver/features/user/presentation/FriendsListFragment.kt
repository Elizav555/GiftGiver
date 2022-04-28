package com.example.giftgiver.features.user.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftgiver.MainActivity
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentFriendsListBinding
import com.example.giftgiver.features.user.domain.UserInfo
import com.example.giftgiver.features.user.presentation.list.UserAdapter
import com.example.giftgiver.features.user.presentation.viewModels.FriendsViewModel
import com.example.giftgiver.utils.BaseFragment
import com.example.giftgiver.utils.autoCleared
import com.example.giftgiver.utils.viewModel

class FriendsListFragment : BaseFragment(R.layout.fragment_friends_list) {
    private lateinit var binding: FragmentFriendsListBinding
    private var userAdapter: UserAdapter by autoCleared()
    private var friends = listOf<UserInfo>()
    private var isAdapterInited = false
    private val friendsViewModel: FriendsViewModel by viewModel()
    private var isFav = false
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
        (activity as MainActivity).setBottomNavigationVisibility(true)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        initObservers()
        friendsViewModel.getFriends()
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
        friendsViewModel.filterFriends(isFav)
        if (isFav) {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_filed,
                null
            )
        } else {
            item.icon = ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_fav_border,
                null
            )
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
        binding.progressBar.isVisible = false
        updateList()
    }

    private fun navigateToProfile(vkId: Long) {
        isAdapterInited = false
        val action = FriendsListFragmentDirections.actionFriendsListFragmentToUserFragment(
            vkId
        )
        findNavController().navigate(action)
    }

    private fun updateList() {
        binding.tvEmpty.isVisible = friends.isEmpty()
        userAdapter.submitList(friends)
    }

    private fun initObservers() {
        friendsViewModel.friends.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                friends = it
                if (isAdapterInited) {
                    updateList()
                } else {
                    isAdapterInited = true
                    init()
                }
            }, onFailure = {
                Log.e("asd", it.message.toString())
            })
        }
    }
}
