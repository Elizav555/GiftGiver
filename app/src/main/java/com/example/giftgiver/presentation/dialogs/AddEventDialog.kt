package com.example.giftgiver.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.giftgiver.R
import com.example.giftgiver.databinding.DialogAddEventBinding
import com.example.giftgiver.domain.entities.Event
import com.example.giftgiver.presentation.fragments.CalendarFragment
import java.util.*

class AddEventDialog : DialogFragment() {
    private lateinit var binding: DialogAddEventBinding
    private val calendar = Calendar.getInstance()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddEventBinding.inflate(layoutInflater)

        with(binding) {
            return activity?.let {
                val dialog = AlertDialog.Builder(it, R.style.MyDialogTheme).setView(root)
                    .setPositiveButton(R.string.add) { _, _ ->
                        (parentFragment as CalendarFragment).addEvent(
                            Event(
                                calendar,
                                etEventDesc.text.toString()
                            )
                        )
                    }
                    .setNegativeButton(
                        R.string.cancel
                    ) { _, _ ->
                        dialog?.cancel()
                    }.create()
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                }
                dialog
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
