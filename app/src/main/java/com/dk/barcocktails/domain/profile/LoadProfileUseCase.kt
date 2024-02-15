package com.dk.barcocktails.domain.profile

import kotlinx.coroutines.flow.Flow

class LoadProfileUseCase(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(): Flow<String> {
        return profileRepository.loadProfile()
    }
}