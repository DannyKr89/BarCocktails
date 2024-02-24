package com.dk.barcocktails.domain.image.usecase

import android.graphics.Bitmap
import com.dk.barcocktails.domain.cocktails.state.LoadingState
import com.dk.barcocktails.domain.image.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class LoadImageUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(bitmap: Bitmap, name: String): Flow<LoadingState<String>> {
        return imageRepository.loadImage(bitmap, name)
    }
}