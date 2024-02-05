package com.dk.barcocktails.data.cocktails

import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class CocktailsRepositoryImpl(private val db: FirebaseFirestore, private val auth: FirebaseAuth) :
    CocktailsRepository {

    private val authUser = auth.currentUser
    override suspend fun getCocktails(): List<Cocktail> {
        val list = mutableListOf<Cocktail>()
        authUser?.let { user ->
            val result =
                db.collection("Users").document(user.uid).collection("Cocktails").get().await()
            result.documents.forEach { snapshot ->
                val cocktail = snapshot.toObject<Cocktail>()
                cocktail?.let {
                    list.add(it)
                }
            }
        }
        return list
    }

    override fun addCocktail(cocktail: Cocktail) {
        authUser?.let { user ->
            val result =
                db.collection("Users").document(user.uid).collection("Cocktails").get()
            result.addOnCompleteListener {
                if (it.isComplete) {
                    cocktail.id = it.result.size()
                    db.collection("Users").document(user.uid).collection("Cocktails").add(
                        cocktail
                    )
                }
            }
        }
    }

    override suspend fun deleteCocktails(cocktail: Cocktail) {

    }
}