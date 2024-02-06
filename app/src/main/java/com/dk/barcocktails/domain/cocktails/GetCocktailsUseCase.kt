package com.dk.barcocktails.domain.cocktails

class GetCocktailsUseCase(private val cocktailsRepository: CocktailsRepository) {

    suspend operator fun invoke(): List<Cocktail> {
        return cocktailsRepository.getCocktails()
    }
}