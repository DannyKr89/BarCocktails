package com.dk.barcocktails.data.cocktails

data class CocktailDTO(
    val name: String?,
    val image: String?,
    val ingredients: HashMap<String, Int>
)