package com.dk.barcocktails.data.image

import android.graphics.Bitmap
import com.dk.barcocktails.domain.image.repository.ImageRepository
import com.dk.barcocktails.domain.state.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.ByteArrayOutputStream

class ImageRepositoryImpl(
    private val auth: FirebaseAuth, private val storage: FirebaseStorage
) : ImageRepository {

    override suspend fun loadImage(bitmap: Bitmap, name: String) = callbackFlow {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
                val data = byteArrayOutputStream.toByteArray()
                val storageRef = storage.reference.child("${user.uid}/$name.jpg")
                storageRef.putBytes(data).addOnProgressListener {
                    trySend(LoadingState.Loading((100 * it.bytesTransferred) / it.totalByteCount))
                }.addOnCompleteListener {
                    storageRef.downloadUrl.addOnCompleteListener {
                        trySend(LoadingState.Success(it.result.toString()))
                    }
                }.addOnFailureListener {
                    trySend(LoadingState.Error(it.fillInStackTrace()))
                }.addOnCanceledListener {
                    storageRef.delete()
                }
            } catch (e: Exception) {
                trySend(LoadingState.Error(e))
            }
        }
        awaitClose()
    }
}