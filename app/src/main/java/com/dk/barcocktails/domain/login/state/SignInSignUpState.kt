package com.dk.barcocktails.domain.login.state

import com.dk.barcocktails.domain.login.model.User

sealed class SignInSignUpState {
    data class Success(val user: User) : SignInSignUpState()
    data class Error(val error: Throwable) : SignInSignUpState()
}