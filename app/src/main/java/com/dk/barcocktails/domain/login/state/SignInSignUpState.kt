package com.dk.barcocktails.domain.login.state

sealed class SignInSignUpState {
    data class Success(val success: String) : SignInSignUpState()
    data class Error(val error: Throwable) : SignInSignUpState()
}