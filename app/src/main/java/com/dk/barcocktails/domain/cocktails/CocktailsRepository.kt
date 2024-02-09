package com.dk.barcocktails.domain.cocktails

import kotlinx.coroutines.flow.Flow

interface CocktailsRepository {

    suspend fun getCocktails(): Flow<LoadingState<List<Cocktail>>>

    suspend fun addCocktail(cocktail: Cocktail): Flow<LoadingState<String>>

    suspend fun deleteCocktails(cocktail: Cocktail)
}