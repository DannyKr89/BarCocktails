package com.dk.barcocktails.data

import com.dk.barcocktails.domain.login.LoginRepository
import kotlinx.coroutines.flow.flow

object FakeRepositoryImpl : LoginRepository {

    private val map: MutableMap<String, String> = mutableMapOf("admin" to "admin")

    override suspend fun signIn(email: String, password: String) = flow {
        emit(map.containsKey(email) && map[email] == password)
    }

    override suspend fun signUp(email: String, password: String) = flow {
        if (map.containsKey(email)) {
            emit(false)
        } else {
            map[email] = password
            emit(true)
        }
    }

    override suspend fun signOut() {

    }
}