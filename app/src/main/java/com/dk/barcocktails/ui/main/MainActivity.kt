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
import androidx.navigation.ui.setupWithNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var requestPermission: ActivityResultLauncher<String>
    private val firebaseAuth = get<FirebaseAuth>()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestStoragePermissions()
        checkStoragePermission()
        setUpToolbar()
        setUpNavigation()
        checkCurentUser(savedInstanceState)
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

    private fun checkCurentUser(savedInstanceState: Bundle?) {
        if (firebaseAuth.currentUser != null && savedInstanceState == null) {
            navController.navigate(R.id.action_loginFragment_to_cocktailsFragment)
        }
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                navView.isVisible = destination.id != R.id.loginFragment
                toolbar.isVisible = destination.id != R.id.loginFragment
            }
            supportActionBar?.title = destination.label
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.isTitleCentered = true
    }
}