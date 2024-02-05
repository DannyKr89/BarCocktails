package com.dk.barcocktails.domain.cocktails

class DeleteCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend fun deleteCocktail(cocktail: Cocktail) {
        return cocktailsRepository.deleteCocktails(cocktail)
    }
}