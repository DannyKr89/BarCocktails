package com.dk.barcocktails.ui.cocktails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.R
import com.dk.barcocktails.common.COCKTAILS
import com.dk.barcocktails.common.USERS
import com.dk.barcocktails.databinding.FragmentCocktailsBinding
import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.dk.barcocktails.ui.main.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CocktailsFragment : Fragment() {

    private var _binding: FragmentCocktailsBinding? = null
    private val binding: FragmentCocktailsBinding get() = _binding!!
    private val firebaseAuth = get<FirebaseAuth>()
    private val db = get<FirebaseFirestore>()
    private lateinit var listener: ListenerRegistration

    private val viewModel: CocktailsViewModel by viewModel()
    private val mainViewModel: MainViewModel by activityViewModel()
    private val adapter: CocktailsAdapter = get()
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCocktailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        checkForUpdate()
    }

    private fun initViews() {
        with(binding) {
            rvCocktails.adapter = adapter
            fabAddCocktail.setOnClickListener {
                findNavController().navigate(R.id.action_cocktails_list_to_newCocktailFragment)
            }
        }
    }

    private fun checkForUpdate() {
        val user = firebaseAuth.currentUser?.uid.toString()
        listener = db.collection(USERS).document(user).collection(COCKTAILS)
            .addSnapshotListener(MetadataChanges.INCLUDE) { _, _ ->
                viewModel.getCocktails()
            }
    }

    private fun initViewModel() {
        mainViewModel.checkOrganization()
        mainViewModel.liveDataCheckOrganization.observe(viewLifecycleOwner) { isOrganization ->
            when (isOrganization) {
                true -> {
                    mainViewModel.liveDataCheckPassword.observe(viewLifecycleOwner) { admin ->
                        when (admin) {
                            true -> {
                                isAdmin = true
                                with(binding) {
                                    fabAddCocktail.isVisible = true
                                }
                            }

                            false -> {
                                isAdmin = false
                                with(binding) {
                                    fabAddCocktail.isVisible = false
                                }
                            }
                        }
                    }
                }

                false -> {
                    with(binding) {
                        isAdmin = true
                        fabAddCocktail.isVisible = !isOrganization
                    }
                }
            }
        }

        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> {
                    Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                }

                is LoadingState.Loading -> {
                    showProgressBar(true)
                }

                is LoadingState.Success -> {
                    showProgressBar(false)
                    showData(state.data)
                }
            }
        }
    }

    private fun showData(data: List<Cocktail>) {
        with(binding) {
            adapter.submitList(data)
            adapter.listener = {
                if (isAdmin) {
                    viewModel.deleteCocktail(it)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.hint_enter_password),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            rvCocktails.postDelayed(
                {
                    rvCocktails.scrollToPosition(0)
                }, 10
            )
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        with(binding) {
            progressbar.isVisible = isLoading
        }
    }

    override fun onDestroyView() {
        binding.rvCocktails.adapter = null
        listener.remove()
        _binding = null
        super.onDestroyView()
    }
}