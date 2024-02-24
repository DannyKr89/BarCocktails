package com.dk.barcocktails.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.FragmentCocktailDetailsBinding
import com.dk.barcocktails.databinding.ItemCocktailIngredientBinding
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.ui.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CocktailDetailsFragment() : BottomSheetDialogFragment() {
    private var _binding: FragmentCocktailDetailsBinding? = null
    private val binding: FragmentCocktailDetailsBinding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCocktailDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        mainViewModel.liveDataCocktail.observe(viewLifecycleOwner) {
            showData(it)
        }
    }

    private fun showData(cocktail: Cocktail?) {
        println(cocktail)
        if (cocktail != null) {
            with(binding) {
                if (cocktail.image.isNotEmpty()) {
                    ivImageCocktail.load(cocktail.image) {
                        crossfade(true)
                        scale(Scale.FILL)
                        transformations(RoundedCornersTransformation(60f))
                    }
                }
                tvName.text = cocktail.name
                cocktail.ingredients.forEach { (name, value) ->
                    val view = LayoutInflater.from(root.context)
                        .inflate(R.layout.item_cocktail_ingredient, llIngredients, false)
                    val ingredientBinding = ItemCocktailIngredientBinding.bind(view)
                    ingredientBinding.apply {
                        tvCocktailIngredient.text = name
                        tvCocktailValue.text = value.toString()
                        llIngredients.addView(view)
                    }
                }
                tvMethod.text = cocktail.method
                tvGarnier.text = cocktail.garnier
                tvDescription.text = cocktail.description
            }
        } else {
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}