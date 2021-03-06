package com.example.giftgiver

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import com.example.giftgiver.utils.AppBarConfig
import com.example.giftgiver.utils.OnAppBarChangesListener
import com.example.giftgiver.utils.ThemeUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var onAppBarChangesListener: OnAppBarChangesListener

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        val navView: BottomNavigationView = binding.bottomNav
        setSupportActionBar(binding.toolbar)
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
        ThemeUtils.isLight = !isNightMode()
        //navView.setOnItemReselectedListener { }
    }

    private fun isNightMode(): Boolean {
        val nightModeFlags: Int =
            this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
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
                    makeToast(getString(R.string.no_internet))
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
        lifecycleScope.launch {
            onAppBarChangesListener.observeToolbarChanges().collect {
                handleToolbarChanges(it)
            }
        }
        lifecycleScope.launch {
            onAppBarChangesListener.observeTitleChanges().collect {
                handleTitleChanges(it)
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

    private fun handleToolbarChanges(appBarConfig: AppBarConfig) {
        with(binding) {
            appBarConfig.firstButton?.let { btn ->
                ivFirst.setImageResource(btn.icon)
                ivFirst.setOnClickListener {
                    btn.action.invoke()
                }
                ivFirst.isVisible = true
            } ?: run { ivFirst.isVisible = false }
            appBarConfig.secondButton?.let { btn ->
                ivSecond.setImageResource(btn.icon)
                ivSecond.setOnClickListener {
                    btn.action.invoke()
                }
                ivSecond.isVisible = true
            } ?: run { ivSecond.isVisible = false }
            appBarConfig.title?.let { tvToolbar.text = it }
            searchView.isVisible = appBarConfig.hasSearch
        }
    }

    private fun handleTitleChanges(title: String) {
        binding.tvToolbar.text = title
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}
