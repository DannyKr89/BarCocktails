package com.dk.barcocktails.ui.cocktails.viewholder

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ItemCocktailBinding
import com.dk.barcocktails.databinding.ItemCocktailIngredientBinding
import com.dk.barcocktails.domain.cocktails.model.Cocktail

class CocktailViewHolder(
    private val binding: ItemCocktailBinding,
    private val imgLoader: ImageLoader,
    private var listener: ((Cocktail) -> Unit)?
) : ItemViewHolder(binding) {

    fun bind(cocktail: Cocktail) {
        with(binding) {
            root.setOnLongClickListener {
                showPopupMenu(it, cocktail)
                true
            }
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
            } else {
                ivImage.load(R.drawable.ic_cocktail)
            }
            tvGarnier.text = cocktail.garnier
            tvMethod.text = cocktail.method
        }
    }

    private fun showPopupMenu(view: View, cocktail: Cocktail) {
        val popupMenu = PopupMenu(view.context, view, Gravity.END)
        popupMenu.inflate(R.menu.context_menu)
        popupMenu.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.delete_item -> {
                    listener?.invoke(cocktail)
                    true
                }

                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }
}