package com.dk.barcocktails.domain.cocktails

sealed class LoadingState {

    data class Success(val data: List<Cocktail>) : LoadingState()
    data class Error(val error: Throwable) : LoadingState()
    object Loading : LoadingState()
}