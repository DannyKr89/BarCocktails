package com.dk.barcocktails.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.yandex.mobile.ads.common.MobileAds
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var requestPermission: ActivityResultLauncher<String>
    private val firebaseAuth: FirebaseAuth = get()
    private val viewModel: MainViewModel by viewModel()

    private val destinationListener by lazy {
        NavController.OnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                navView.isVisible =
                    destination.id != R.id.loginFragment && destination.id != R.id.signUpFragment
            }
            supportActionBar?.title = destination.label
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestStoragePermissions()
        checkStoragePermission()
        setUpToolbar()
        setUpNavigation()
        checkCurrentUser(savedInstanceState)
        MobileAds.initialize(this) {
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun requestStoragePermissions() {
        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    }

    private fun checkCurrentUser(savedInstanceState: Bundle?) {
        if (firebaseAuth.currentUser != null && savedInstanceState == null) {
            navController.navigate(R.id.action_loginFragment_to_cocktailsFragment)
        }
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.loginFragment, R.id.cocktails_list, R.id.profile))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener(destinationListener)
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.isTitleCentered = true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}