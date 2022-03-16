package com.example.giftgiver.presentation.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.giftgiver.R
import com.example.giftgiver.databinding.FragmentCalendarBinding
import com.example.giftgiver.utils.ClientState

class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private val client = ClientState.client

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }
}
