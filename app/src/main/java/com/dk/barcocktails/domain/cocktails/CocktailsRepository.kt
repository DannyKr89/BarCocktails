package com.dk.barcocktails.domain.cocktails

interface CocktailsRepository {

    suspend fun getCocktails(): List<Cocktail>

    suspend fun addCocktail(cocktail: Cocktail): LoadingState<String>

    suspend fun deleteCocktails(cocktail: Cocktail)
}