package com.dk.barcocktails.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentSignUpBinding
import com.dk.barcocktails.domain.state.LoadingState
import com.dk.barcocktails.ui.utils.validator.ErrorEnum
import com.dk.barcocktails.ui.utils.validator.InputType
import com.dk.barcocktails.ui.utils.validator.Validator
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                is LoadingState.Error -> {}
                is LoadingState.Success -> {
                    findNavController().popBackStack()
                }

                is LoadingState.Loading -> {

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
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val name = etName.text.toString().trim()
                val adminPassword = etAdminPassword.text.toString().trim()

                val emailValidator = Validator().check(email, InputType.EMAIL)
                val passwordValidator = Validator().check(password, InputType.PASSWORD)
                val adminPasswordValidator = Validator().check(adminPassword, InputType.PASSWORD)

                if (emailValidator.first
                    && passwordValidator.first
                    && name.isNotEmpty()
                    && adminPasswordValidator.first
                    && password != adminPassword
                ) {
                    viewModel.signUpRequest(email, password, name, adminPassword)
                } else if (password == adminPassword) {
                    tilAdminPassword.error = resources.getString(R.string.equal_password)
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
                    tilName.error = when (name.isEmpty()) {
                        true -> resources.getString(R.string.require_field)
                        false -> null
                    }
                    tilAdminPassword.error = when (adminPasswordValidator.second) {
                        ErrorEnum.REQUIRE -> resources.getString(R.string.require_field)
                        ErrorEnum.VALID -> resources.getString(R.string.valid_password)
                        null -> null
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}