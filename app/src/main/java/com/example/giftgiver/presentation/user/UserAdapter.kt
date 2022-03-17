package com.example.giftgiver.presentation.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemUserBinding
import com.example.giftgiver.domain.entities.User

class UserAdapter(
    private val action: (position: Int) -> Unit,
    private val friends: List<User>,
) : ListAdapter<User, UserHolder>(UserDiffItemCallback()), Filterable {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder = UserHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        action
    )

    private var newFriends: List<User> = friends

    override fun onBindViewHolder(
        holder: UserHolder,
        position: Int
    ) {
        val user = newFriends[position]
        holder.bind(user)
    }

    override fun submitList(list: List<User>?) {
        if (list != null) {
            newFriends = list
        }
        super.submitList(if (list == null) null else ArrayList(list))
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val results = FilterResults()
                if (charSequence == null || charSequence.isEmpty()) {
                    results.values = friends
                    results.count = friends.size
                } else {
                    val filterResultsData = mutableListOf<User>()
                    friends.forEach {
                        if (it.name.contains(charSequence)) {
                            filterResultsData.add(it)
                        }
                    }
                    results.values = filterResultsData
                    results.count = filterResultsData.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                submitList(results.values as List<User>)
            }
        }
    }
}
