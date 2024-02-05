package com.dk.barcocktails.domain.cocktails

class AddCocktailUseCase(private val cocktailsRepository: CocktailsRepository) {

    fun addCocktail(cocktail: Cocktail) {
        return cocktailsRepository.addCocktail(cocktail)
    }
}