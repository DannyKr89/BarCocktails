package com.dk.barcocktails.domain.cocktails.usecase

import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.repository.CocktailsRepository
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

class GetCocktailsUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(): Flow<LoadingState<List<Cocktail>>> {
        return cocktailsRepository.getCocktails()
    }
}