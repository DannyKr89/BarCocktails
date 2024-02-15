package com.dk.barcocktails.domain.login

import kotlinx.coroutines.flow.Flow

class SignUpUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ): Flow<Boolean> {
        return loginRepository.signUp(email, password, name, adminPassword)
    }
}