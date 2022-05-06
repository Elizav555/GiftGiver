package com.example.giftgiver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.giftgiver.common.viewModels.MainViewModel
import com.example.giftgiver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNav
        val navController =
            binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.account,
                R.id.friends_list,
                R.id.calendar,
                R.id.cart
            )
        )
        navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        actionBar?.setHomeButtonEnabled(true)
        navView.setOnItemReselectedListener { }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            mainViewModel.isClientChanged().collect {
                if (it) {
                    mainViewModel.onClientChanged()
                }
            }
        }
        lifecycleScope.launch {
            mainViewModel.hasInternetConnection().collect {
                if (!it) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.no_internet),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        lifecycleScope.launch {
            mainViewModel.isClientAuth().collect {
                if (it == false) {
                    VK.logout()
                    mainViewModel.restart()
                    startFrom(applicationContext)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setBottomNavigationVisibility(isVisible: Boolean) {
        binding.bottomNav.isVisible = isVisible
    }

    fun makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG)
            .show()
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}
