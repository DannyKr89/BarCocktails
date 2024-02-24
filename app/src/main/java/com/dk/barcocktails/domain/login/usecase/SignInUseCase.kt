package com.dk.barcocktails.domain.login.usecase

import com.dk.barcocktails.domain.login.repository.LoginRepository
import com.dk.barcocktails.domain.login.state.SignInSignUpState
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<SignInSignUpState> {
        return loginRepository.signIn(email, password)
    }
}