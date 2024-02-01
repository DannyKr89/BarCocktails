package com.dk.barcocktails.data.login

import android.util.Log
import com.dk.barcocktails.domain.login.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(private val auth: FirebaseAuth) : LoginRepository {

    override suspend fun signIn(email: String, password: String) = flow<Boolean> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("FBA-Success", result.user.toString())
            emit(true)
        } catch (e: Exception) {
            Log.d("FBA-Error", e.message.toString())
            emit(false)
        }
    }

    override suspend fun signUp(email: String, password: String) = flow<Boolean> {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("FBA-Success", result.user.toString())
            emit(true)
        } catch (e: Exception) {
            Log.d("FBA-Error", e.message.toString())
            emit(false)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}