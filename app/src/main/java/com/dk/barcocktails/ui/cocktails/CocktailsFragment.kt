package com.dk.barcocktails.ui.cocktails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dk.barcocktails.databinding.FragmentCocktailsBinding

class CocktailsFragment : Fragment() {

    private var _binding: FragmentCocktailsBinding? = null
    private val binding: FragmentCocktailsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCocktailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}