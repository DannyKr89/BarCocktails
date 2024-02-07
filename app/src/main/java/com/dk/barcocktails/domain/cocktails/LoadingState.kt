package com.dk.barcocktails.domain.cocktails

sealed class LoadingState<out T> {

    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val error: Throwable) : LoadingState<Nothing>()
    data object Loading : LoadingState<Nothing>()
}