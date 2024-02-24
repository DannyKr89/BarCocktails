package com.dk.barcocktails.data.login

import android.util.Log
import com.dk.barcocktails.common.ADMIN_PASSWORD
import com.dk.barcocktails.common.NAME
import com.dk.barcocktails.common.ORGANIZATION
import com.dk.barcocktails.common.USERS
import com.dk.barcocktails.domain.login.model.User
import com.dk.barcocktails.domain.login.repository.LoginRepository
import com.dk.barcocktails.domain.login.state.SignInSignUpState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : LoginRepository {

    override suspend fun signIn(email: String, password: String) = flow<SignInSignUpState> {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("FBA-Success", result.user.toString())
            val user = User(result.user?.uid)
            emit(SignInSignUpState.Success(user))
        } catch (e: Exception) {
            Log.d("FBA-Error", e.message.toString())
            emit(SignInSignUpState.Error(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        adminPassword: String
    ) = flow<SignInSignUpState> {
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
                emit(SignInSignUpState.Success(User(userUid)))
            }
        } catch (e: Exception) {
            emit(SignInSignUpState.Error(e))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}