package com.dk.barcocktails.ui.newcocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dk.barcocktails.AddCocktailService
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentNewCocktailBinding
import com.dk.barcocktails.databinding.ItemIngredientBinding

class NewCocktailFragment : Fragment() {

    private var _binding: FragmentNewCocktailBinding? = null
    private val binding: FragmentNewCocktailBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewCocktailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

    }

    private fun initViews() {
        with(binding) {
            addView()
            fabAddIngredient.setOnClickListener {
                addView()
            }
            btnAddCocktail.setOnClickListener {
                val name = etName.text.toString()
                val method = etMethod.text.toString()
                val garnier = etGarnier.text.toString()
                val ingredients = HashMap<String, Int>()

                llIngredients.forEach {
                    val binding = ItemIngredientBinding.bind(it)
                    ingredients[binding.etIngredientName.text.toString()] =
                        binding.etIngredientValue.text.toString().toInt()
                }
                requireActivity().startService(
                    AddCocktailService.newIntent(
                        requireContext(),
                        name,
                        ingredients,
                        method,
                        garnier
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun addView() {
        var id = 0
        with(binding) {
            val ingredient = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_ingredient, llIngredients, false)
            ingredient.id = id++
            llIngredients.addView(ingredient)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}