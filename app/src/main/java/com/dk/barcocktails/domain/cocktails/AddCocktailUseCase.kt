package com.dk.barcocktails.domain.cocktails

class AddCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(cocktail: Cocktail): LoadingState<String> {
        return cocktailsRepository.addCocktail(cocktail)
    }
}