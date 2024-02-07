package com.dk.barcocktails.data.cocktails

import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class CocktailsRepositoryImpl(
    private val db: FirebaseFirestore,
    auth: FirebaseAuth
) :
    CocktailsRepository {

    private val authUser = auth.currentUser
    override suspend fun getCocktails(): List<Cocktail> {
        val list = mutableListOf<Cocktail>()
        authUser?.let { user ->
            val result =
                db.collection(USERS).document(user.uid).collection(COCKTAILS).get().await()
            result.documents.forEach { snapshot ->
                val cocktail = snapshot.toObject<Cocktail>()
                cocktail?.let {
                    list.add(it)
                }
            }
        }
        return list
    }

    override suspend fun addCocktail(cocktail: Cocktail): LoadingState<String> {
        authUser?.let { user ->
            val idRequest =
                db.collection(USERS).document(user.uid).collection(COCKTAILS).get().await()
            cocktail.id = idRequest.size()
            val addRequest = db.collection(USERS).document(user.uid).collection(COCKTAILS).add(
                cocktail
            ).await()
            return LoadingState.Success("Success")
        }
        return LoadingState.Loading
    }


    override suspend fun deleteCocktails(cocktail: Cocktail) {

    }

    companion object {
        private const val USERS = "Users"
        private const val COCKTAILS = "Cocktails"

    }

}