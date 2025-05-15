package com.anish.gitsearch.domain.repository

import kotlinx.coroutines.flow.Flow
import com.anish.gitsearch.domain.model.User
import com.anish.gitsearch.util.Resource


interface AuthRepository {
    val currentUser: Flow<User?>
    val isAuthenticated: Flow<Boolean>

    suspend fun signInWithGoogle(idToken: String): Resource<User>
    suspend fun signOut()
}