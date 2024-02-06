package com.dk.barcocktails.domain.cocktails

class DeleteCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(cocktail: Cocktail) {
        return cocktailsRepository.deleteCocktails(cocktail)
    }
}