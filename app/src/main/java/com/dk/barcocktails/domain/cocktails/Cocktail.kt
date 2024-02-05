package com.dk.barcocktails.domain.cocktails

data class Cocktail(
    var id: Int? = 0,
    val name: String? = "",
    val image: String? = "",
    val ingredients: HashMap<String, Int> = hashMapOf(),
    val method: String? = "",
    val garnier: String? = "-",
)
