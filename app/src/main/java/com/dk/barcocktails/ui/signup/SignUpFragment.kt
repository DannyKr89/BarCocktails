package com.dk.barcocktails.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentSignUpBinding
import com.dk.barcocktails.domain.login.state.SignInSignUpState
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : ScopeFragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel.signUpState.observe(viewLifecycleOwner) {
            when (it) {
                is SignInSignUpState.Error -> {}
                is SignInSignUpState.Success -> {
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            swOrganization.setOnCheckedChangeListener { _, isChecked ->
                tilName.isVisible = isChecked
                tilAdminPassword.isVisible = isChecked
            }
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val name = etName.text.toString()
                val adminPassword = etAdminPassword.text.toString()

                if (etEmail.text.isNullOrEmpty()) {
                    etEmail.error = resources.getString(R.string.require_field)
                } else if (etPassword.text.isNullOrEmpty()) {
                    etPassword.error = resources.getString(R.string.require_field)
                } else {
                    viewModel.signUpRequest(email, password, name, adminPassword)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}