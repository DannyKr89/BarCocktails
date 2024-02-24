package com.dk.barcocktails.domain.login.usecase

import com.dk.barcocktails.domain.login.repository.LoginRepository

class SignOutUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke() {
        loginRepository.signOut()
    }
}