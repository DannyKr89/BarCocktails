package com.dk.barcocktails.data.image

import android.net.Uri
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.dk.barcocktails.domain.image.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl(
    auth: FirebaseAuth, private val storage: FirebaseStorage
) : ImageRepository {

    private val authUser = auth.currentUser
    override suspend fun loadImage(uri: String): LoadingState<String> {
        authUser?.let { user ->
            val file = Uri.fromFile(File(uri))
            val storageRef = storage.reference.child("${user.uid}/${file.lastPathSegment}")
            val request = storageRef.putFile(file).await()
            if (request.task.isSuccessful) {
                val uriTask = storageRef.downloadUrl.await()
                return LoadingState.Success(uriTask.toString())
            } else {
                if (request.error != null) {
                    return LoadingState.Error(Throwable(request.error?.message))
                }
            }
        }
        return LoadingState.Loading
    }
}