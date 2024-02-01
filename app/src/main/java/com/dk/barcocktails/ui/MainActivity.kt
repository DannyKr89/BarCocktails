package com.dk.barcocktails.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val firebaseAuth = get<FirebaseAuth>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.isVisible = destination.id != R.id.loginFragment
        }

        if (firebaseAuth.currentUser != null) {
            navController.navigate(R.id.action_loginFragment_to_cocktailsFragment)
        }
    }
}