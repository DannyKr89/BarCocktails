package com.dk.barcocktails.domain.login.usecase

import com.dk.barcocktails.domain.login.repository.LoginRepository
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<Boolean> {
        return loginRepository.signIn(email, password)
    }
}