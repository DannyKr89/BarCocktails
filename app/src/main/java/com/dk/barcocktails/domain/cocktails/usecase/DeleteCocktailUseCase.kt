package com.dk.barcocktails.domain.cocktails.usecase

import com.dk.barcocktails.domain.cocktails.model.Cocktail
import com.dk.barcocktails.domain.cocktails.repository.CocktailsRepository

class DeleteCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(cocktail: Cocktail) {
        return cocktailsRepository.deleteCocktails(cocktail)
    }
}