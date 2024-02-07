package com.dk.barcocktails.domain.image

import com.dk.barcocktails.domain.cocktails.LoadingState

interface ImageRepository {

    suspend fun loadImage(uri: String): LoadingState<String>
}