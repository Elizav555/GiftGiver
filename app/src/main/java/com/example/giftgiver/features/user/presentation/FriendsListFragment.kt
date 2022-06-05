package com.example.giftgiver.features.user.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.DrawableRes
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
import com.example.giftgiver.utils.*
import javax.inject.Inject

class FriendsListFragment : BaseFragment(R.layout.fragment_friends_list) {
    private lateinit var binding: FragmentFriendsListBinding
    private var userAdapter: UserAdapter by autoCleared()
    private var friends = listOf<UserInfo>()
    private var isAdapterInited = false
    private val friendsViewModel: FriendsViewModel by viewModel()
    private var isFav = false

    @Inject
    lateinit var appBarChangesListener: OnAppBarChangesListener
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
        (activity as MainActivity).supportActionBar?.show()
        appBarChangesListener.onToolbarChanges(
            setAppBarConfig(R.drawable.ic_fav_border, "Friends List")
        )
        configSearch()
        initObservers()
        friendsViewModel.getFriends()
    }

    private fun configSearch() {
        (activity as? MainActivity)?.findViewById<SearchView>(R.id.search_view)?.apply {
            setOnQueryTextListener(
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
            setOnSearchClickListener {
                appBarChangesListener.onToolbarChanges(setAppBarConfig(title = ""))
            }
            setOnCloseListener {
                appBarChangesListener.onToolbarChanges(
                    setAppBarConfig(
                        R.drawable.ic_fav_border,
                        "Friends List"
                    )
                )
                false
            }
        }
    }

    private fun setAppBarConfig(
        @DrawableRes favDrawableRes: Int? = null,
        title: String
    ): AppBarConfig {
        val config = AppBarConfig(
            firstButton = null,
            title = title,
            hasSearch = true
        )
        favDrawableRes?.let { config.firstButton = AppBarButton(favDrawableRes, ::filterFriends) }
        return config
    }

    private fun filterFriends() {
        isFav = !isFav
        friendsViewModel.filterFriends(isFav)
        var title = "Friends List"
        val favRes = if (isFav) {
            title = "Favorites List"
            R.drawable.ic_fav_filed
        } else R.drawable.ic_fav_border
        appBarChangesListener.onToolbarChanges(setAppBarConfig(favRes, title))
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
        (activity as? MainActivity)?.findViewById<SearchView>(R.id.search_view)
            ?.onActionViewCollapsed()
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
