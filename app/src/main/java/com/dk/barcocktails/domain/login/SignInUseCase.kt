package com.dk.barcocktails.domain.login

import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val loginRepository: LoginRepository) {

    suspend fun signIn(email: String, password: String): Flow<Boolean> {
        return loginRepository.signIn(email, password)
    }
}