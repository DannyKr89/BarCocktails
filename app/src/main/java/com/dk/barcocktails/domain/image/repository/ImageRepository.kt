package com.dk.barcocktails.domain.image.repository

import android.graphics.Bitmap
import com.dk.barcocktails.domain.cocktails.state.LoadingState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun loadImage(bitmap: Bitmap, name: String): Flow<LoadingState<String>>
}