package com.dk.barcocktails.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.get

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val firebaseAuth = get<FirebaseAuth>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tv.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_profile_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}