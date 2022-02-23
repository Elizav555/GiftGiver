package com.example.giftgiver.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.giftgiver.databinding.ItemUserBinding

class UserAdapter(
    private val action: (position: Int) -> Unit,
    private val friends: List<User>,
) : ListAdapter<User, UserHolder>(UserDiffItemCallback()) {

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

    override fun onBindViewHolder(
        holder: UserHolder,
        position: Int
    ) {
        val user = friends[position]
        holder.bind(user)
    }

    override fun submitList(list: List<User>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}
