package com.dk.barcocktails.domain.state

sealed class LoadingState<out T> {

    data class Success<T>(val data: T) : LoadingState<T>()
    data class Error(val error: Throwable) : LoadingState<Nothing>()
    data class Loading(val progress: Long? = null) : LoadingState<Nothing>()
}