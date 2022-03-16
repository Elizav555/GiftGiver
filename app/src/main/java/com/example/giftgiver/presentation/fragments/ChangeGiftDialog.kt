package com.example.giftgiver.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.giftgiver.R
import com.example.giftgiver.databinding.DialogAddGiftBinding

class ChangeGiftDialog : DialogFragment() {
    private lateinit var binding: DialogAddGiftBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddGiftBinding.inflate(layoutInflater)
// TODO: load image from phone or link or make photo
        with(binding) {
            return activity?.let {
                val dialog = AlertDialog.Builder(it).setView(root)
                    .setPositiveButton("Save changes") { _, _ ->
                        (parentFragment as GiftFragment).changeGift(
                            etName.text.toString(),
                            etDesc.text.toString(),
                            etImageLink.text.toString()
                        )
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        dialog?.cancel()
                    }.create()
                etName.addTextChangedListener {
                    checkInputs(dialog)
                }
                etDesc.addTextChangedListener {
                    checkInputs(dialog)
                }
                etImageLink.addTextChangedListener {
                    checkInputs(dialog)
                }
                dialog.setOnShowListener {
                    checkInputs(dialog)
                }
                dialog
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }

    private fun checkInputs(dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = with(binding) {
            etName.text.toString().isNotBlank() && etDesc.text.toString()
                .isNotBlank() && etImageLink.text.toString().isNotBlank()
        }
    }
}
