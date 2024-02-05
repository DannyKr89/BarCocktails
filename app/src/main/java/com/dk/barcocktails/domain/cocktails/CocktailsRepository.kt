package com.dk.barcocktails.domain.cocktails

interface CocktailsRepository {

    suspend fun getCocktails(): List<Cocktail>

    fun addCocktail(cocktail: Cocktail)

    suspend fun deleteCocktails(cocktail: Cocktail)
}