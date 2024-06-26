package com.dk.barcocktails.ui.signinsignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentLoginBinding
import com.dk.barcocktails.domain.login.SignInSignUpState
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel.signInState.observe(viewLifecycleOwner) {
            when (it) {
                is SignInSignUpState.Error -> {
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT)
                        .show()
                }

                SignInSignUpState.Init -> {
                    Toast.makeText(requireContext(), "Init", Toast.LENGTH_SHORT).show()
                }

                is SignInSignUpState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_cocktailsFragment)
                }
            }
        }
        viewModel.signUpState.observe(viewLifecycleOwner) {
            when (it) {
                is SignInSignUpState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

                SignInSignUpState.Init -> {}
                is SignInSignUpState.Success -> {
                    Toast.makeText(requireContext(), "User Created", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun initViews() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (etEmail.text.isEmpty()) {
                    etEmail.error = resources.getString(R.string.require_field)
                } else if (etPassword.text.isEmpty()) {
                    etPassword.error = resources.getString(R.string.require_field)
                } else {
                    viewModel.signInRequest(email, password)
                }
            }
            tvSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (etEmail.text.isEmpty()) {
                    etEmail.error = resources.getString(R.string.require_field)
                } else if (etPassword.text.isEmpty()) {
                    etPassword.error = resources.getString(R.string.require_field)
                } else {
                    viewModel.signUpRequest(email, password)
                }
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}