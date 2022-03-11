package com.example.giftgiver.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.FirestoreDB
import com.example.giftgiver.data.firebase.entities.ClientFB
import com.example.giftgiver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firestore = FirestoreDB()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.account,
                R.id.wishlists,
                R.id.friends,
                R.id.calendar,
                R.id.cart
            )
        )
        navView.setupWithNavController(navController)
        checkUser()
    }

    private fun checkUser() {
        val vkId = VK.getUserId().value
        val ref = firestore.clients.whereEqualTo("vkId", vkId).get().result
        if (ref == null) {
            firestore.addNewClient(ClientFB(vkId))
        }
    }
}
