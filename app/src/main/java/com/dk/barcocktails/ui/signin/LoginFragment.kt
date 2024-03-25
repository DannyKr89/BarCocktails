package com.dk.barcocktails.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentLoginBinding
import com.dk.barcocktails.domain.state.LoadingState
import com.dk.barcocktails.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                is LoadingState.Error -> {
                    Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
                }

                is LoadingState.Success -> {
                    mainViewModel.user(it.data)
                    findNavController().navigate(R.id.action_loginFragment_to_cocktailsFragment)
                }

                is LoadingState.Loading -> {

                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.signInRequest(email, password)
                } else {
                    tilEmail.error = when (email.isNotEmpty()) {
                        true -> null
                        false -> resources.getString(R.string.require_field)
                    }
                    tilPassword.error = when (password.isNotEmpty()) {
                        true -> null
                        false -> resources.getString(R.string.require_field)
                    }
                }
            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}