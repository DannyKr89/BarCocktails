package com.dk.barcocktails.ui.cocktails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dk.barcocktails.R
import com.dk.barcocktails.databinding.ItemCocktailBinding
import com.dk.barcocktails.domain.cocktails.Cocktail

class CocktailsAdapter : ListAdapter<Cocktail, CocktailsAdapter.CoctailViewHolder>(comparator) {

    class CoctailViewHolder(private val binding: ItemCocktailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cocktail: Cocktail) {
            with(binding) {
                llIngredients.removeAllViews()
                tvName.text = cocktail.name
                cocktail.ingredients.forEach { name, value ->
                    val textview = TextView(binding.root.context)
                    textview.text = "$name $value"
                    llIngredients.addView(textview)
                }
                tvGarnier.text = cocktail.garnier
                tvMethod.text = cocktail.method
                ivImage.load(R.drawable.ic_cocktail)
            }
        }

    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<Cocktail>() {
            override fun areItemsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Cocktail, newItem: Cocktail): Boolean {
                return oldItem.id == newItem.id
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoctailViewHolder {
        val binding =
            ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoctailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoctailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


