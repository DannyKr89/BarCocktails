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
import com.dk.barcocktails.domain.login.state.SignInSignUpState
import com.dk.barcocktails.ui.utils.validator.ErrorEnum
import com.dk.barcocktails.ui.utils.validator.Validator
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

                is SignInSignUpState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_cocktailsFragment)
                }
            }
        }
    }

    private fun initViews() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                val emailValidator = Validator.validateEmail(email)
                val passwordValidator = Validator.validateLoginPassword(password)

                if (emailValidator.first && passwordValidator.first) {
                    viewModel.signInRequest(email, password)
                } else {
                    tilEmail.error = when (emailValidator.second) {
                        ErrorEnum.REQUIRE -> resources.getString(R.string.require_field)
                        ErrorEnum.VALID -> resources.getString(R.string.valid_email)
                        null -> null
                    }
                    tilPassword.error = when (passwordValidator.second) {
                        ErrorEnum.REQUIRE -> resources.getString(R.string.require_field)
                        ErrorEnum.VALID -> resources.getString(R.string.valid_password)
                        null -> null
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