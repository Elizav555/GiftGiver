package com.example.giftgiver.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.giftgiver.R
import com.example.giftgiver.data.firebase.ClientsRepositoryImpl
import com.example.giftgiver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val clientsRep = ClientsRepositoryImpl()
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
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
        lifecycleScope.launch { checkUser() }
    }

    private suspend fun checkUser() {
        val vkId = VK.getUserId().value
        val res = withContext(dispatcher) {
            clientsRep.getClientByVkId(vkId)
        }.result
        //todo как вернуть))))))
        // if (res==null)
        // clientsRep.addClient(Client(vkId, Calendar(),LoadUserInfoVK().loadInfo(vkId)))
    }
}
