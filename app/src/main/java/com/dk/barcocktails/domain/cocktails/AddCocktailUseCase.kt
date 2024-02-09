package com.dk.barcocktails.domain.cocktails

import kotlinx.coroutines.flow.Flow

class AddCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(cocktail: Cocktail): Flow<LoadingState<String>> {
        return cocktailsRepository.addCocktail(cocktail)
    }
}