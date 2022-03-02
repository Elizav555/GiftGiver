package com.example.giftgiver.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.giftgiver.R
import com.example.giftgiver.firebase.RealtimeDB
import com.vk.api.sdk.VK

class AccountFragment : Fragment() {

    private val database = RealtimeDB()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database.addNewClient(VK.getUserId().value, "Interesting Info", listOf())
    }
}
