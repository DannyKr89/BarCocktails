package com.dk.barcocktails.domain.profile

import kotlinx.coroutines.flow.Flow


class CheckOrganizationUseCase(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(): Flow<Boolean> {
        return profileRepository.checkOrganization()
    }
}