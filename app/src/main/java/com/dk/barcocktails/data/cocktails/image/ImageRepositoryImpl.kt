package com.dk.barcocktails.data.cocktails.image

import android.net.Uri
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.dk.barcocktails.domain.image.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl(
    auth: FirebaseAuth, private val storage: FirebaseStorage
) : ImageRepository {

    private val authUser = auth.currentUser
    override suspend fun loadImage(uri: String) = flow<LoadingState<String>> {
        authUser?.let { user ->
            try {
                val file = Uri.fromFile(File(uri))
                val storageRef = storage.reference.child("${user.uid}/${file.lastPathSegment}")
                val request = storageRef.putFile(file).await()
                if (request.task.isSuccessful) {
                    val uriTask = storageRef.downloadUrl.await()
                    emit(LoadingState.Success(uriTask.toString()))
                }
            } catch (e: Exception) {
                emit(LoadingState.Error(e))
            }

        }
    }
}