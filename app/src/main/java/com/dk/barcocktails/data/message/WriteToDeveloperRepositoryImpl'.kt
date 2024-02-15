package com.dk.barcocktails.data.message

import com.dk.barcocktails.common.ADMIN
import com.dk.barcocktails.common.MESSAGE
import com.dk.barcocktails.common.USERS
import com.dk.barcocktails.domain.message.WriteToDeveloperRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class WriteToDeveloperRepositoryImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : WriteToDeveloperRepository {
    override suspend fun sendMessage(message: String) = flow {
        val authUser = auth.currentUser
        authUser?.let {
            try {
                val map = hashMapOf(
                    MESSAGE to message
                )
                db.collection(USERS).document(ADMIN).collection(MESSAGE).document(authUser.uid)
                    .set(map).await()
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }

        }
    }
}