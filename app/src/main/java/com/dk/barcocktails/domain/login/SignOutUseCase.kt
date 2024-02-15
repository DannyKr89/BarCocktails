package com.dk.barcocktails.domain.login

class SignOutUseCase(private val loginRepository: LoginRepository) {

    suspend operator fun invoke() {
        loginRepository.signOut()
    }
}