package com.dk.barcocktails.ui.cocktails

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ItemCocktailBinding
import com.dk.barcocktails.databinding.ItemCocktailIngredientBinding
import com.dk.barcocktails.domain.cocktails.model.Cocktail

class CocktailsAdapter(private val imgLoader: ImageLoader) :
    ListAdapter<Cocktail, CocktailsAdapter.CocKtailViewHolder>(comparator) {

    var listener: ((Cocktail) -> Unit)? = null

    inner class CocKtailViewHolder(private val binding: ItemCocktailBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocKtailViewHolder {
        val binding =
            ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CocKtailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CocKtailViewHolder, position: Int) {
        holder.bind(getItem(position))
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


