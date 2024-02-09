package com.dk.barcocktails.domain.image

import com.dk.barcocktails.domain.cocktails.LoadingState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun loadImage(uri: String): Flow<LoadingState<String>>
}