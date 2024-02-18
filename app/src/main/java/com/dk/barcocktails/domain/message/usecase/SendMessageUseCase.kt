package com.dk.barcocktails.domain.message.usecase

import com.dk.barcocktails.domain.message.repository.WriteToDeveloperRepository
import kotlinx.coroutines.flow.Flow

class SendMessageUseCase(private val repository: WriteToDeveloperRepository) {

    suspend operator fun invoke(message: String): Flow<Boolean> {
        return repository.sendMessage(message)
    }
}