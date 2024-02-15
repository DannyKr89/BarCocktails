package com.dk.barcocktails.domain.login

import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun signIn(email: String, password: String): Flow<Boolean>
    suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ): Flow<Boolean>

    suspend fun signOut()
}