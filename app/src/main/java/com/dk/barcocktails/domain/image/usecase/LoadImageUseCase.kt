package com.dk.barcocktails.domain.image.usecase

import com.dk.barcocktails.domain.cocktails.state.LoadingState
import com.dk.barcocktails.domain.image.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class LoadImageUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(uri: String): Flow<LoadingState<String>> {
        return imageRepository.loadImage(uri)
    }
}