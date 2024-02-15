package com.dk.barcocktails.data.profile

import com.dk.barcocktails.domain.profile.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) : ProfileRepository {

    override suspend fun loadProfile() = flow {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val result = db.collection(USERS).document(user.uid).get().await()
                val name = result.get(NAME)
                if (name != null) {
                    emit(name.toString())
                } else {
                    emit("Not an organization")
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun checkPassword(password: String) = flow {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val result = db.collection(USERS).document(user.uid).get().await()
                val passwordResult = result.get(PASSWORD).toString()
                if (passwordResult == password) {
                    emit(true)
                } else {
                    emit(false)
                }

            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun checkOrganization() = flow {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val result = db.collection(USERS).document(user.uid).get().await()
                val isOrganization = result.get(ORGANIZATION)
                if (isOrganization != null) {
                    emit(isOrganization as Boolean)
                } else {
                    emit(false)
                }
            } catch (e: Exception) {
                throw e
            }

        }
    }

    companion object {
        private const val USERS = "Users"
        private const val NAME = "Name"
        private const val ORGANIZATION = "Organization"
        private const val PASSWORD = "Admin password"
    }
}