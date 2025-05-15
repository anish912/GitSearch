package com.anish.gitsearch.data.repository

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
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val githubService: GithubService,
    private val repositoryDao: RepositoryDao,
    private val authRepository: AuthRepository
) : GithubRepository {

    override fun getUserRepositories(forceRefresh: Boolean): Flow<Resource<List<Repository>>> = flow {
        // This is just a stub for now
        emit(Resource.Loading())
    }

    override fun searchRepositories(query: String): Flow<Resource<List<Repository>>> = flow {
        // This is just a stub for now
        emit(Resource.Loading())
    }

    override suspend fun clearUserData() {
        // This is just a stub for now
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