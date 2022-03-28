package com.example.giftgiver.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.giftgiver.R
import com.example.giftgiver.databinding.DialogAddWishlistBinding
import com.example.giftgiver.presentation.fragments.WishlistFragment

class ChangeWishlistDialog : DialogFragment() {
    private lateinit var binding: DialogAddWishlistBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddWishlistBinding.inflate(layoutInflater)

        with(binding) {
            return activity?.let {
                val dialog = AlertDialog.Builder(it,R.style.MyDialogTheme).setView(root)
                    .setPositiveButton("Change name") { _, _ ->
                        (parentFragment as WishlistFragment).changeWishlistName(
                            etName.text.toString()
                        )
                    }
                    .setNegativeButton(
                        R.string.cancel
                    ) { _, _ ->
                        dialog?.cancel()
                    }.create()
                etName.addTextChangedListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                        etName.text.toString().isNotBlank()
                }
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                        etName.text.toString().isNotBlank()
                }
                dialog
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
