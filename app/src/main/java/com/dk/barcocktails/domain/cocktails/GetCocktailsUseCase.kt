package com.dk.barcocktails.domain.cocktails

class GetCocktailsUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend fun getCocktails(): List<Cocktail> {
        return cocktailsRepository.getCocktails()
    }
}