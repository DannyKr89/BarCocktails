package com.dk.barcocktails.domain.message

import kotlinx.coroutines.flow.Flow

interface WriteToDeveloperRepository {

    suspend fun sendMessage(message: String): Flow<Boolean>
}