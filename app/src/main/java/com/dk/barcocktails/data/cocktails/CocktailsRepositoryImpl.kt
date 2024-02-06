package com.dk.barcocktails.data.cocktails

import android.net.Uri
import com.dk.barcocktails.domain.cocktails.Cocktail
import com.dk.barcocktails.domain.cocktails.CocktailsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File


class CocktailsRepositoryImpl(
    private val db: FirebaseFirestore,
    auth: FirebaseAuth,
    private val storage: FirebaseStorage
) :
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

    override suspend fun addCocktail(cocktail: Cocktail) {
        authUser?.let { user ->
            val file = Uri.fromFile(File(cocktail.image.toString()))
            val riversRef = storage.reference.child("${user.uid}/${file.lastPathSegment}")
            val requst = riversRef.putFile(file).await()
            val uriTask = riversRef.downloadUrl.await()

            cocktail.image = uriTask.toString()
            val result =
                db.collection("Users").document(user.uid).collection("Cocktails").get().await()

            cocktail.id = result.size()
            db.collection("Users").document(user.uid).collection("Cocktails").add(
                cocktail
            ).await()
        }

    }


    override suspend fun deleteCocktails(cocktail: Cocktail) {

    }
}