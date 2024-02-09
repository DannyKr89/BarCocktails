package com.dk.barcocktails.domain.cocktails

import kotlinx.coroutines.flow.Flow

class GetCocktailsUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(): Flow<LoadingState<List<Cocktail>>> {
        return cocktailsRepository.getCocktails()
    }
}