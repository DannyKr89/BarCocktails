package com.dk.barcocktails.domain.profile.usecase

import com.dk.barcocktails.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class CheckPasswordUseCase(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(password: String): Flow<Boolean> {
        return profileRepository.checkPassword(password)
    }
}