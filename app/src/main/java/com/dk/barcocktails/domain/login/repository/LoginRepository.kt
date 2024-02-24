package com.dk.barcocktails.domain.login.repository

import com.dk.barcocktails.domain.login.state.SignInSignUpState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Flow<SignInSignUpState>
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ): Flow<SignInSignUpState>

    suspend fun signOut()
}