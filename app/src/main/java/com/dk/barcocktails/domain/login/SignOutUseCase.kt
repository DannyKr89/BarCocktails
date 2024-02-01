package com.dk.barcocktails.domain.login

class SignOutUseCase(private val loginRepository: LoginRepository) {

    suspend fun signOut() {
        loginRepository.signOut()
    }
}