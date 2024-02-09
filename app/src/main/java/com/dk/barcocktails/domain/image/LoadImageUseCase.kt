package com.dk.barcocktails.domain.image

import com.dk.barcocktails.domain.cocktails.LoadingState
import kotlinx.coroutines.flow.Flow

class LoadImageUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(uri: String): Flow<LoadingState<String>> {
        return imageRepository.loadImage(uri)
    }
}