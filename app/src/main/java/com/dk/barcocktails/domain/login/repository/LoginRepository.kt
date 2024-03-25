package com.dk.barcocktails.domain.login.repository

import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Flow<LoadingState<User>>
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ): Flow<LoadingState<User>>

    suspend fun signOut()
}