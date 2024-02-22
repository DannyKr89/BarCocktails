package com.dk.barcocktails.ui.profile

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentProfileBinding
import com.dk.barcocktails.ui.main.MainViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFragment : MenuProvider, Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()
    private val viewModel: ProfileViewModel = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        initViewModel()
        initViews()
    }

    private fun initViews() {
        with(binding) {
            etOrganization.isEnabled = false
            btnConfirm.setOnClickListener {
                mainViewModel.checkPassword(etPassword.text.toString())
            }
            tvWrite.setOnClickListener {
                findNavController().navigate(R.id.action_profile_to_writeToDeveloperDialogFragment)
            }
        }
    }

    private fun initViewModel() {
        viewModel.liveDataLoadProfile.observe(viewLifecycleOwner) {
            binding.etOrganization.text = SpannableStringBuilder(it)
        }
        mainViewModel.liveDataCheckOrganization.observe(viewLifecycleOwner) { isOrganization ->
            mainViewModel.checkPassword(mainViewModel.pass)
            mainViewModel.liveDataCheckPassword.observe(viewLifecycleOwner) { isAdmin ->
                setupHint(isOrganization, isAdmin)
            }
        }

    }

    private fun setupHint(isOrganization: Boolean, isAdministrator: Boolean) {
        with(binding) {
            btnConfirm.isEnabled = isOrganization
            tilPassword.isEnabled = isOrganization
            if (isOrganization && isAdministrator) {
                binding.tvHint.text = resources.getString(R.string.hint_is_admin)
            } else if (isOrganization != isAdministrator) {
                binding.tvHint.text = resources.getString(R.string.hint_enter_password)
            } else {
                binding.tvHint.text = resources.getString(R.string.hint_not_organization)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_profile, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.sign_out -> {
                mainViewModel.checkPassword("")
                viewModel.signOut()
                findNavController().navigate(R.id.action_profile_to_loginFragment)
                return true
            }
        }
        return false
    }
}