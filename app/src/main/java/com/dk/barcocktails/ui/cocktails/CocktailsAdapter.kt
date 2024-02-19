package com.dk.barcocktails.ui.cocktails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import coil.ImageLoader
import com.dk.barcocktails.databinding.ItemBannerBinding
import com.dk.barcocktails.databinding.ItemCocktailBinding
import com.dk.barcocktails.domain.cocktails.model.Banner
import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.model.Item
import com.dk.barcocktails.ui.cocktails.viewholder.BannerViewHolder
import com.dk.barcocktails.ui.cocktails.viewholder.CocktailViewHolder
import com.dk.barcocktails.ui.cocktails.viewholder.ItemViewHolder

class CocktailsAdapter(private val imgLoader: ImageLoader) :
    ListAdapter<Item, ItemViewHolder>(comparator) {

    var listener: ((Cocktail) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            ITEM_BANNER -> {
                val binding =
                    ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BannerViewHolder(binding)
            }

            ITEM_COCKTAIL -> {
                val binding =
                    ItemCocktailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CocktailViewHolder(binding, imgLoader, listener)
            }

            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder) {
            is BannerViewHolder -> {
                holder.bind()
            }

            is CocktailViewHolder -> {
                holder.bind(getItem(position) as Cocktail)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            Banner -> ITEM_BANNER
            is Cocktail -> ITEM_COCKTAIL
        }
    }

    companion object {

        const val ITEM_BANNER = 342
        const val ITEM_COCKTAIL = 745

        val comparator = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}


