package com.dk.barcocktails.data.cocktails.image

import android.net.Uri
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.dk.barcocktails.domain.image.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class ImageRepositoryImpl(
    auth: FirebaseAuth, private val storage: FirebaseStorage
) : ImageRepository {

    private val authUser = auth.currentUser
    override suspend fun loadImage(uri: String) = callbackFlow {
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