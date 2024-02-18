package com.dk.barcocktails.domain.image.repository

import com.dk.barcocktails.domain.cocktails.state.LoadingState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun loadImage(uri: String): Flow<LoadingState<String>>
}