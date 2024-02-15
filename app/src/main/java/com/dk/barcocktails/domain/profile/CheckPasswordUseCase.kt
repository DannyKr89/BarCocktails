package com.dk.barcocktails.domain.profile

import kotlinx.coroutines.flow.Flow

class CheckPasswordUseCase(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(password: String): Flow<Boolean> {
        return profileRepository.checkPassword(password)
    }
}