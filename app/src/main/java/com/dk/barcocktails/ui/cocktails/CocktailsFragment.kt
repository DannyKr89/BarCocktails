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
import com.dk.barcocktails.databinding.FragmentCocktailsBinding
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class CocktailsFragment : Fragment() {

    private var _binding: FragmentCocktailsBinding? = null
    private val binding: FragmentCocktailsBinding get() = _binding!!
    private val firebaseAuth = get<FirebaseAuth>()
    private val db = get<FirebaseFirestore>()
    private lateinit var listener: ListenerRegistration

    private val viewModel: CocktailsViewModel by viewModel()
    private val adapter = CocktailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCocktailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = firebaseAuth.currentUser?.uid.toString()

        initViewModel()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_cocktails_list_to_newCocktailFragment)
        }

        listener = db.collection("Users").document(user).collection("Cocktails")
            .addSnapshotListener { _, _ ->
                viewModel.getCocktails()
            }
    }

    private fun initViewModel() {
        viewModel.liveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState.Error -> {
                    Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
                }

                LoadingState.Loading -> {
                    showProgressBar(true)
                }

                is LoadingState.Success -> {
                    showProgressBar(false)
                    with(binding) {
                        rvCocktails.adapter = adapter
                        adapter.submitList(state.data.sortedBy { it.id }.asReversed())
                    }
                }
            }
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        with(binding) {
            progressbar.isVisible = isLoading
        }

    }

    override fun onDestroyView() {
        listener.remove()
        super.onDestroyView()
    }
}