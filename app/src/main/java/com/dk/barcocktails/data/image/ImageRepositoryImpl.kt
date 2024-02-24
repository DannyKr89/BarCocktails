package com.dk.barcocktails.data.image

import android.net.Uri
import android.util.Log
import com.dk.barcocktails.domain.cocktails.state.LoadingState
import com.dk.barcocktails.domain.image.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.File

class ImageRepositoryImpl(
    private val auth: FirebaseAuth, private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun loadImage(uri: String) = callbackFlow {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val file = Uri.fromFile(File(uri))
                val storageRef = storage.reference.child("${user.uid}/${file.lastPathSegment}")
                storageRef.putFile(file).addOnProgressListener {
                    trySend(LoadingState.Loading((100 * it.bytesTransferred) / it.totalByteCount))
                }.addOnCompleteListener {
                    storageRef.downloadUrl.addOnCompleteListener {
                        trySend(LoadingState.Success(it.result.toString()))
                    }
                }.addOnFailureListener {
                    trySend(LoadingState.Error(it.fillInStackTrace()))
                }
            } catch (e: Exception) {
                trySend(LoadingState.Error(e))
            }
        }
        awaitClose()
    }
}