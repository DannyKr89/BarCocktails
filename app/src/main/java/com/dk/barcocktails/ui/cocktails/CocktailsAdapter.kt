package com.dk.barcocktails.ui.cocktails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ItemCocktailBinding
import com.dk.barcocktails.databinding.ItemCocktailIngredientBinding
import com.dk.barcocktails.domain.cocktails.Cocktail

class CocktailsAdapter(private val imgLoader: ImageLoader) :
    ListAdapter<Cocktail, CocktailsAdapter.CocKtailViewHolder>(comparator) {

    inner class CocKtailViewHolder(private val binding: ItemCocktailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cocktail: Cocktail) {
            with(binding) {
                llIngredients.removeAllViews()
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
                if (cocktail.image.isNotEmpty()) {
                    ivImage.load(cocktail.image, imgLoader) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(20f))
                        placeholder(R.drawable.ic_cocktail)
                    }
                }

                tvGarnier.text = cocktail.garnier
                tvMethod.text = cocktail.method
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocKtailViewHolder {
        val binding =
            ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CocKtailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CocKtailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<Cocktail>() {
            override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
                return oldItem == newItem
            }

        }

    }
}


