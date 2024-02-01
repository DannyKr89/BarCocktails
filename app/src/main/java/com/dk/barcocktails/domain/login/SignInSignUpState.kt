package com.dk.barcocktails.domain.login

sealed class SignInSignUpState {
    data object Init : SignInSignUpState()
    data class Success(val success: String) : SignInSignUpState()
    data class Error(val error: Throwable) : SignInSignUpState()
}