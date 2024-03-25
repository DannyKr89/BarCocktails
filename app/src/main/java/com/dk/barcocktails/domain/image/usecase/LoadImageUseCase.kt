package com.dk.barcocktails.domain.image.usecase

import android.graphics.Bitmap
import com.dk.barcocktails.domain.image.repository.ImageRepository
import com.dk.barcocktails.domain.state.LoadingState
import kotlinx.coroutines.flow.Flow

class LoadImageUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(bitmap: Bitmap, name: String): Flow<LoadingState<String>> {
        return imageRepository.loadImage(bitmap, name)
    }
}