package com.dk.barcocktails.domain.image

import com.dk.barcocktails.domain.cocktails.LoadingState

class LoadImageUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(uri: String): LoadingState<String> {
        return imageRepository.loadImage(uri)
    }
}