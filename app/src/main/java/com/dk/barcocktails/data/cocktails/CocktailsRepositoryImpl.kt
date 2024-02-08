package com.dk.barcocktails.data.cocktails

import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.dk.barcocktails.domain.cocktails.LoadingState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class CocktailsRepositoryImpl(
    private val db: FirebaseFirestore, auth: FirebaseAuth
) : CocktailsRepository {

    private val authUser = auth.currentUser
    override suspend fun getCocktails(): List<Cocktail> {
        val list = mutableListOf<Cocktail>()
        authUser?.let { user ->
            val result = db.collection(USERS).document(user.uid).collection(COCKTAILS)
                .orderBy(ID, Query.Direction.DESCENDING).get().await()
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
            var id = 0
            val idRequest =
                db.collection(USERS).document(user.uid).collection(COCKTAILS).get().await()
            idRequest.forEach {
                if (it.id.toInt() > id) {
                    id = it.id.toInt()
                }
            }
            cocktail.id = id
            val addRequest =
                db.collection(USERS).document(user.uid).collection(COCKTAILS).add(cocktail).await()
            return LoadingState.Success("Success")
        }
        return LoadingState.Loading
    }


    override suspend fun deleteCocktails(cocktail: Cocktail) {

    }

    companion object {
        private const val USERS = "Users"
        private const val COCKTAILS = "Cocktails"
        private const val ID = "id"
    }

}