package com.dk.barcocktails.domain.profile.usecase

import com.dk.barcocktails.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow


class CheckOrganizationUseCase(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(): Flow<Boolean> {
        return profileRepository.checkOrganization()
    }
}