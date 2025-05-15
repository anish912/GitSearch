package com.anish.gitsearch.domain.repository

import com.anish.gitsearch.domain.model.Repository
import com.anish.gitsearch.util.Resource
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun getUserRepositories(forceRefresh: Boolean = false): Flow<Resource<List<Repository>>>
    fun searchRepositories(query: String): Flow<Resource<List<Repository>>>
    suspend fun clearUserData()
}