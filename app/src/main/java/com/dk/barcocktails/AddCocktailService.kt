package com.dk.barcocktails

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.dk.barcocktails.domain.cocktails.AddCocktailUseCase
import com.dk.barcocktails.domain.cocktails.Cocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class AddCocktailService : IntentService(NAME) {

    private val addCocktailUseCase: AddCocktailUseCase = get()

    override fun onHandleIntent(intent: Intent?) {
        val name = intent?.getStringExtra(COCKTAIL_NAME)
        val image = intent?.getStringExtra(COCKTAIL_IMAGE)
        val ingredients = intent?.getSerializableExtra(COCKTAIL_INGREDIENTS) as HashMap<String, Int>
        val method = intent.getStringExtra(COCKTAIL_METHOD)
        val garnier = intent.getStringExtra(COCKTAIL_GARNIER)
        val cocktail = Cocktail(
            0,
            name,
            image,
            ingredients,
            method,
            garnier
        )
        CoroutineScope(Dispatchers.IO).launch {
            addCocktailUseCase.invoke(cocktail)
        }
    }

    companion object {
        private const val NAME = "Add Cocktail"
        private const val COCKTAIL_NAME = "name"
        private const val COCKTAIL_INGREDIENTS = "ingredients"
        private const val COCKTAIL_METHOD = "method"
        private const val COCKTAIL_GARNIER = "garnier"
        private const val COCKTAIL_IMAGE = "image"

        fun newIntent(
            context: Context,
            name: String,
            image: String,
            ingredients: HashMap<String, Int>,
            method: String,
            garnier: String
        ) =
            Intent(context, AddCocktailService::class.java).apply {
                putExtra(COCKTAIL_NAME, name)
                putExtra(COCKTAIL_IMAGE, image)
                putExtra(COCKTAIL_INGREDIENTS, ingredients)
                putExtra(COCKTAIL_METHOD, method)
                putExtra(COCKTAIL_GARNIER, garnier)
            }
    }
}