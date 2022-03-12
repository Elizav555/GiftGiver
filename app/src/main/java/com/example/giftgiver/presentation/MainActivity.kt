package com.example.giftgiver.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.databinding.ActivityMainBinding
import com.example.giftgiver.domain.entities.Calendar
import com.example.giftgiver.domain.entities.Cart
import com.example.giftgiver.domain.entities.Client
import com.example.giftgiver.domain.usecase.LoadUserInfoVK
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val clientsRep = ClientsRepositoryImpl()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.account,
//                R.id.wishlists,
//                R.id.friends,
//                R.id.calendar,
//                R.id.cart
//            )
//        )
        navView.setupWithNavController(navController)

        val vkId = VK.getUserId().value
        lifecycleScope.launch {
            checkUser(vkId).addOnSuccessListener {
                if (it.data == null) {
                    lifecycleScope.launch {
                        clientsRep.addClient(
                            Client(
                                vkId,
                                Calendar(),
                                LoadUserInfoVK().loadInfo(vkId),
                                Cart()
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun checkUser(vkId: Long) =
        withContext(scope.coroutineContext) {
            clientsRep.getClientByVkId(vkId)
        }
}
