package com.dk.barcocktails.data.cocktails

import com.dk.barcocktails.common.COCKTAILS
import com.dk.barcocktails.common.ID
import com.dk.barcocktails.common.USERS
import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class CocktailsRepositoryImpl(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) : CocktailsRepository {

    override suspend fun getCocktails() = flow<LoadingState<List<Cocktail>>> {
        emit(LoadingState.Loading())
        val authUser = auth.currentUser
        val list = mutableListOf<Cocktail>()
        authUser?.let { user ->
            try {
                val result = db.collection(USERS).document(user.uid).collection(COCKTAILS)
                    .orderBy(ID, Query.Direction.DESCENDING).get().await()
                result.documents.forEach { snapshot ->
                    val cocktail = snapshot.toObject<Cocktail>()
                    cocktail?.let {
                        list.add(it)
                    }
                }
                emit(LoadingState.Success(list))
            } catch (e: Exception) {
                emit(LoadingState.Error(e))
            }

        }
    }

    override suspend fun addCocktail(cocktail: Cocktail) = flow {
        emit(LoadingState.Loading())
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val idRequest =
                    db.collection(USERS).document(user.uid).collection(COCKTAILS).get().await()
                idRequest.forEach {
                    if (it.id.toInt() >= cocktail.id) {
                        cocktail.id = it.id.toInt() + 1
                    }
                }
                val addRequest = db.collection(USERS).document(user.uid).collection(COCKTAILS)
                    .document(cocktail.id.toString()).set(cocktail).await()
                emit(LoadingState.Success("Success"))
            } catch (e: Exception) {
                emit(LoadingState.Error(e))
            }

        }
    }


    override suspend fun deleteCocktails(cocktail: Cocktail) {
        val authUser = auth.currentUser
        authUser?.let { user ->
            try {
                val idRequest = db.collection(USERS).document(user.uid).collection(COCKTAILS)
                    .document(cocktail.id.toString()).delete()
            } catch (e: Exception) {
                throw e
            }
        }
    }

}