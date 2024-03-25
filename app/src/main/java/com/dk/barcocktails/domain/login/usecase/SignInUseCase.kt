package com.dk.barcocktails.domain.login.usecase

import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.login.repository.LoginRepository
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<LoadingState<User>> {
        return loginRepository.signIn(email, password)
    }
}