package com.dk.barcocktails.domain.profile.repository

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun loadProfile(): Flow<String>
    suspend fun checkPassword(password: String): Flow<Boolean>
    suspend fun checkOrganization(): Flow<Boolean>
}