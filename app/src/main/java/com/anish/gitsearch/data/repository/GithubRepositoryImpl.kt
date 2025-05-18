package com.anish.gitsearch.data.repository

import android.util.Log
import com.anish.gitsearch.data.local.RepositoryDao
import com.anish.gitsearch.data.local.RepositoryEntity
import com.anish.gitsearch.data.remote.GithubService
import com.anish.gitsearch.data.remote.RepositoryDto
import com.anish.gitsearch.domain.model.Owner
import com.anish.gitsearch.domain.model.Repository
import com.anish.gitsearch.domain.repository.AuthRepository
import com.anish.gitsearch.domain.repository.GithubRepository
import com.anish.gitsearch.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val githubService: GithubService,
    private val repositoryDao: RepositoryDao,
    private val authRepository: AuthRepository
) : GithubRepository {

    override fun getUserRepositories(forceRefresh: Boolean): Flow<Resource<List<Repository>>> = flow {
            emit(Resource.Loading())
            try {
                val localRepositories = repositoryDao.getRepositories().map { entities ->
                    entities.map { it.toDomainModel() }
                }

                    val cachedData = localRepositories.first()
                    if (cachedData.isNotEmpty() && !forceRefresh) {
                        emit(Resource.Success(cachedData))
                    }
                    if (forceRefresh || cachedData.isEmpty()) {
                        val user = authRepository.currentUser.first()
                        if (user != null) {
                            val userForSearch = user.name.split(" ").firstOrNull() ?: user.name.replace(" ","")                               // In a real app, you'd get this from
                            val remoteRepositories = githubService.getUserRepositories(userForSearch)

                            val sorted= remoteRepositories.sortedByDescending { it.stargazers_count }
                            Log.d("anish", "Remote repositories: $remoteRepositories")

                            // Save to database
                            repositoryDao.insertRepositories(sorted.map { it.toEntity() })

                            // Emit refreshed data
                            emit(Resource.Success(remoteRepositories.map { it.toDomainModel() }))
                        } else {
                            Timber.e("User not authenticated")
                            emit(Resource.Error("User not authenticated"))
                        }

                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error fetching repositories")
                    emit(Resource.Error("Error fetching repositories"))
                }

            }



    override fun searchRepositories(query: String): Flow<Resource<List<Repository>>> = flow {
        // This is just a stub for now
        emit(Resource.Loading())
        try {
            // First try searching in local database
            val localResults = repositoryDao.searchRepositories("%$query%").first()

            // If local results exist, emit them first
            if (localResults.isNotEmpty()) {
                emit(Resource.Success(localResults.map { it.toDomainModel() }))
            }

            // Always fetch from remote for search
            val user = authRepository.currentUser.first()
            if (user != null) {
                val token = "Bearer token_goes_here" // In a real app, you'd get this from auth
                val remoteResults = githubService.searchRepositories(query)

                // Save to database
                repositoryDao.insertRepositories(remoteResults.items.map { it.toEntity() })

                // Emit remote results
                emit(Resource.Success(remoteResults.items.map { it.toDomainModel() }))
            } else {
                if (localResults.isEmpty()) {
                    emit(Resource.Error("User not authenCcated"))
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error searching repositories")
            emit(Resource.Error(e.message ?: "Failed to search repositories"))
        }
    }

    override suspend fun clearUserData() {
        try {
            val user = authRepository.currentUser.first()
            if (user!=null){
                repositoryDao.clearUserRepositories(user.id)
                repositoryDao.clearAllRepositories()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error clearing user data")
        }
    }

    // Mapping functions
    private fun RepositoryDto.toDomainModel(): Repository {
        return Repository(
            id = id,
            name = name,
            description = description,
            starCount = stargazers_count,
            language = language,
            owner = Owner(
                login = owner.login,
                avatarUrl = owner.avatar_url
            )
        )
    }

    private fun RepositoryDto.toEntity(): RepositoryEntity {
        return RepositoryEntity(
            id = id,
            name = name,
            description = description,
            starCount = stargazers_count,
            language = language,
            ownerLogin = owner.login,
            ownerAvatarUrl = owner.avatar_url
        )
    }

    private fun RepositoryEntity.toDomainModel(): Repository {
        return Repository(
            id = id,
            name = name,
            description = description,
            starCount = starCount,
            language = language,
            owner = Owner(
                login = ownerLogin,
                avatarUrl = ownerAvatarUrl
            )
        )
    }
}