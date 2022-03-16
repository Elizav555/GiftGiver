package com.example.giftgiver.presentation.gift

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.databinding.ItemGiftBinding
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.entities.Gift
import com.example.giftgiver.domain.repositories.ClientsRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext

class GiftHolder(
    private val binding: ItemGiftBinding,
    action: (position: Int) -> Unit,
    private val checkedFunc: ((Int, Boolean)->Unit)?,
    private val getClient: ((Long) -> Deferred<Client?>)?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            action(adapterPosition)
        }
    }

    fun bind(gift: Gift) {
        with(binding) {
            if (checkedFunc!=null) {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked = gift.isChosen
                if (gift.isChosen) {
                    ivFilter.visibility = View.VISIBLE
                    checkBox.isClickable = false
                }
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

                }
            }
            if (getClient!=null) {
                tvFor.visibility = View.VISIBLE
              //  getClient(gift.forUser)
            }
            tvName.text = gift.name
            ivPhoto.load(gift.imageUrl)
        }
    }
}
