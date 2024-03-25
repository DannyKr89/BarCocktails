package com.dk.barcocktails.data.login

import android.util.Log
import com.dk.barcocktails.common.ADMIN_PASSWORD
import com.dk.barcocktails.common.NAME
import com.dk.barcocktails.common.ORGANIZATION
import com.dk.barcocktails.common.USERS
import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.login.repository.LoginRepository
import com.dk.barcocktails.domain.state.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : LoginRepository {

    override suspend fun signIn(email: String, password: String) = flow {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("FBA-Success", result.user.toString())
            val user = User(result.user?.uid)
            emit(LoadingState.Success(user))
        } catch (e: Exception) {
            Log.d("FBA-Error", e.message.toString())
            emit(LoadingState.Error(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ) = flow {
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
                emit(LoadingState.Success(User(userUid)))
            }
        } catch (e: Exception) {
            emit(LoadingState.Error(e))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}