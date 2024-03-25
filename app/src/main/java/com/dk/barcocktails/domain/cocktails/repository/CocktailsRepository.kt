package com.dk.barcocktails.domain.cocktails.repository

import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

interface CocktailsRepository {

    suspend fun getCocktails(): Flow<LoadingState<List<Cocktail>>>

    suspend fun addCocktail(cocktail: Cocktail): Flow<LoadingState<String>>

    suspend fun deleteCocktails(cocktail: Cocktail)
}