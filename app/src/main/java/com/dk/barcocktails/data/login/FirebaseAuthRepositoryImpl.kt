package com.dk.barcocktails.data.login

import android.util.Log
import com.dk.barcocktails.domain.login.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : LoginRepository {

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

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ) = flow<Boolean> {
        val user = hashMapOf<String, Any>()
        if (name.isEmpty() || adminPassword.isEmpty()) {
            user[ORGANIZATION] = false
        } else {
            user[ORGANIZATION] = true
            user[NAME] = name
            user[ADMIN_PASSWORD] = adminPassword
        }
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userUid = result.user?.uid
            userUid?.let {
                db.collection(USERS).document(it).set(user).await()
                emit(true)
            }
        } catch (e: Exception) {
            emit(false)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    companion object {
        private const val USERS = "Users"
        private const val ORGANIZATION = "Organization"
        private const val NAME = "Name"
        private const val ADMIN_PASSWORD = "Admin password"
    }
}