package com.example.giftgiver.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.giftgiver.R
import com.example.giftgiver.databinding.DialogAddWishlistBinding
import com.example.giftgiver.domain.entities.Wishlist

class AddWishlistDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddWishlistBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddWishlistBinding.inflate(layoutInflater)

        with(binding) {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setView(root)
                    .setPositiveButton(R.string.add) { _, _ ->
                        AccountFragment().addWishlist(
                            Wishlist(
                                0,
                                etName.text.toString(),
                                listOf()
                            )
                        )
                    }
                    .setNegativeButton(
                        R.string.cancel
                    ) { _, _ ->
                        dialog?.cancel()
                    }
                val dialog = builder.create()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    etName.text.toString().isNotBlank()
                dialog
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
