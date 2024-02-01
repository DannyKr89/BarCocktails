package com.dk.barcocktails.domain.login

import kotlinx.coroutines.flow.Flow

class SignUpUseCase(private val loginRepository: LoginRepository) {

    suspend fun signUp(email: String, password: String): Flow<Boolean> {
        return loginRepository.signUp(email, password)
    }
}