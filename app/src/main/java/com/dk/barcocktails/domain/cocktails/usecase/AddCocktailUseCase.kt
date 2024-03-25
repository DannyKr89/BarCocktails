package com.dk.barcocktails.domain.cocktails.usecase

import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.repository.CocktailsRepository
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

class AddCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(cocktail: Cocktail): Flow<LoadingState<String>> {
        return cocktailsRepository.addCocktail(cocktail)
    }
}